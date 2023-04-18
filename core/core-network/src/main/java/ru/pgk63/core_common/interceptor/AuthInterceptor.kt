package ru.pgk63.core_common.interceptor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.pgk63.core_common.api.auth.AuthApi
import ru.pgk63.core_common.api.auth.model.SignIn
import ru.pgk63.core_common.common.Constants.CUSTOM_HEADER
import ru.pgk63.core_common.common.Constants.NO_AUTH
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Provider

internal class AuthInterceptor @Inject constructor(
    private val userDataSource: UserDataSource,
    private val authApi: Provider<AuthApi>
): Interceptor {

    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()

        if (NO_AUTH in req.headers.values(CUSTOM_HEADER)) {
            return chain.proceedWithToken(req, null)
        }

        val token = userDataSource.getAccessToken()

        val res = chain.proceedWithToken(req, token)

        if (res.code != HttpURLConnection.HTTP_UNAUTHORIZED || token == null) {
            return res
        }

        val newToken: String? = runBlocking(Dispatchers.IO) {
            mutex.withLock {
                val maybeUpdatedToken = userDataSource.getAccessToken()

                when {
                    maybeUpdatedToken == null -> null
                    maybeUpdatedToken != token -> maybeUpdatedToken
                    else -> {
                        val refreshTokenRes = authApi.get().getAccessToken(
                            refreshToken = userDataSource.getRefreshToken().toString()
                        )

                        when (refreshTokenRes.code()) {
                            HttpURLConnection.HTTP_OK -> {
                                refreshTokenRes.body()!!.accessToken.also { updatedToken ->
                                    userDataSource.saveAccessToken(updatedToken)
                                }
                            }
                            HttpURLConnection.HTTP_INTERNAL_ERROR, HttpURLConnection.HTTP_UNAUTHORIZED -> {

                                val user = userDataSource.get().first()

                                val signInResponse = authApi.get().signIn(SignIn(
                                    firstName = user.firstName,
                                    lastName = user.lastName,
                                    password = user.password
                                ))
                                val accessToken = signInResponse.body()?.accessToken

                                userDataSource.saveAccessToken(accessToken)
                                userDataSource.saveRefreshToken(signInResponse.body()?.refreshToken)

                                if(accessToken == null){
                                    userDataSource.save(UserLocalDatabase(statusRegistration = false))
                                }

                                accessToken
                            }
                            else -> {
                                null
                            }
                        }
                    }
                }
            }
        }

        return if (newToken !== null) chain.proceedWithToken(req, newToken) else res
    }

    private fun Interceptor.Chain.proceedWithToken(req: Request, token: String?): Response =
        req.newBuilder()
            .apply {
                if (token !== null) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .removeHeader(CUSTOM_HEADER)
            .build()
            .let(::proceed)

}