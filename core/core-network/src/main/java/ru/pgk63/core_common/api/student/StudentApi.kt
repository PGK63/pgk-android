package ru.pgk63.core_common.api.student

import retrofit2.Response
import retrofit2.http.*
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.user.model.UserRegistrationResponse

interface StudentApi {

    @POST("/pgk63/api/Student/Registration")
    suspend fun registration(
        @Body body: ru.pgk63.core_model.student.StudentRegistrationBody
    ): Response<UserRegistrationResponse>

    @GET("/pgk63/api/Student")
    suspend fun getAll(
        @Query("search") search:String? = null,
        @Query("groupIds") groupIds:List<Int>? = null,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = Constants.PAGE_SIZE
    ): ru.pgk63.core_model.student.StudentResponse

    @GET("/pgk63/api/Student/{id}")
    suspend fun getById(
        @Path("id") id:Int
    ): Response<ru.pgk63.core_model.student.Student>
}