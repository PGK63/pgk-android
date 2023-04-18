package ru.pgk63.core_common.api.auth.repository

import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.api.auth.AuthApi
import ru.pgk63.core_common.api.auth.model.SignIn
import ru.pgk63.core_common.api.auth.model.SignInResponse
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.enums.theme.ThemeCorners
import ru.pgk63.core_common.enums.theme.ThemeFontSize
import ru.pgk63.core_common.enums.theme.ThemeFontStyle
import ru.pgk63.core_common.enums.theme.ThemeStyle
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val userDataSource: UserDataSource
): ApiResponse() {

    suspend fun signIn(body: SignIn): Result<SignInResponse> {
        val response = safeApiCall { authApi.signIn(body) }

        if(response is Result.Success && response.data?.accessToken != null && response.data.errorMessage == null){
            val userLocalDatabase = UserLocalDatabase(
                statusRegistration = true,
                userId = response.data.userId,
                groupId = response.data.groupId,
                userRole = response.data.userRole,
                darkMode = response.data.darkMode,
                themeStyle = response.data.themeStyle,
                themeFontStyle = response.data.themeFontStyle,
                themeFontSize = response.data.themeFontSize,
                themeCorners = response.data.themeCorners,
                languageCode = response.data.language?.code,
                emailVerification = response.data.emailVerification,
                email = response.data.email,
                telegramId = response.data.telegramId,
                firstName = body.firstName,
                lastName = body.lastName,
                password = body.password,
                refreshToken = response.data.refreshToken,
                accessToken = response.data.accessToken
            )

            userDataSource.save(userLocalDatabase)
            userDataSource.saveAccessToken(response.data.accessToken)
            userDataSource.saveRefreshToken(response.data.refreshToken)
        }

        return response
    }

    suspend fun revokeRefreshToken() : Result<Unit?> = safeApiCall {
        authApi.revokeRefreshToken(refreshToken = userDataSource.getRefreshToken()!!)
    }

    suspend fun getAccessToken(refreshToken:String) = authApi.getAccessToken(refreshToken)
}