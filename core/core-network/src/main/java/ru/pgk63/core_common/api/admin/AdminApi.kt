package ru.pgk63.core_common.api.admin

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.api.user.model.UserRegistrationResponse

interface AdminApi {

    @POST("/pgk63/api/Admin/Registration")
    suspend fun registration(
        @Body body: UserRegistrationBody
    ): Response<UserRegistrationResponse>
}