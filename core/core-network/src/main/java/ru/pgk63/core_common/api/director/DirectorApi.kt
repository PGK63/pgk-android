package ru.pgk63.core_common.api.director

import retrofit2.Response
import retrofit2.http.*
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.director.model.Director
import ru.pgk63.core_common.api.director.model.DirectorResponse
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.api.user.model.UserRegistrationResponse

interface DirectorApi {

    @GET("/pgk63/api/Director")
    suspend fun getAll(
        @Query("search") search: String?,
        @Query("current") current: Boolean?,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int = PAGE_SIZE
    ): DirectorResponse

    @GET("/pgk63/api/Director/{id}")
    suspend fun getById(
        @Path("id") id: Int
    ): Response<Director>

    @POST("/pgk63/api/Director/Registration")
    suspend fun registration(
        @Body body: UserRegistrationBody
    ): Response<UserRegistrationResponse>

    @PATCH("/pgk63/api/Director/{id}/Current")
    suspend fun updateCurrent(
        @Path("id") id: Int
    ): Response<Unit?>
}