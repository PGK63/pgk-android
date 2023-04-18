package ru.pgk63.core_common.api.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.pgk63.core_common.api.auth.model.AccessToken
import ru.pgk63.core_common.api.auth.model.SignIn
import ru.pgk63.core_common.api.auth.model.SignInResponse
import ru.pgk63.core_common.common.Constants.CUSTOM_HEADER
import ru.pgk63.core_common.common.Constants.NO_AUTH

interface AuthApi {

    @Headers("$CUSTOM_HEADER: $NO_AUTH")
    @POST("/pgk63/api/Auth/SignIn")
    suspend fun signIn(@Body body: SignIn) : Response<SignInResponse>

    @Headers("$CUSTOM_HEADER: $NO_AUTH")
    @POST("/pgk63/api/Auth/Revoke")
    suspend fun revokeRefreshToken(@Header("refreshToken") refreshToken:String):Response<Unit?>

    @Headers("$CUSTOM_HEADER: $NO_AUTH")
    @POST("/pgk63/api/Auth/Refresh")
    suspend fun getAccessToken(@Header("refreshToken") refreshToken:String): Response<AccessToken>
}