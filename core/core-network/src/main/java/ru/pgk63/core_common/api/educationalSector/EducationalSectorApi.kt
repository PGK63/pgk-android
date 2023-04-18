package ru.pgk63.core_common.api.educationalSector

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.api.user.model.UserRegistrationResponse

interface EducationalSectorApi {

    @POST("/pgk63/api/EducationalSector/Registration")
    suspend fun registration(
        @Body body: UserRegistrationBody
    ): Response<UserRegistrationResponse>
}