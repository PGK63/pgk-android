package ru.pgk63.core_common.api.teacher

import retrofit2.Response
import retrofit2.http.*
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.teacher.model.ResponseTeacher
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.api.user.model.UserRegistrationResponse

interface TeacherApi {

    @POST("/pgk63/api/Teacher/Registration")
    suspend fun registration(
        @Body body: UserRegistrationBody
    ): Response<UserRegistrationResponse>

    @GET("/pgk63/api/Teacher")
    suspend fun getAll(
        @Query("search") search:String? = null,
        @Query("subjectIds") subjectIds:List<Int>? = null,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = Constants.PAGE_SIZE
    ): ResponseTeacher

    @GET("/pgk63/api/Teacher/{id}")
    suspend fun getById(
        @Path("id") id: Int
    ): Response<Teacher>

    @POST("/pgk63/api/Teacher/{id}/Subject")
    suspend fun teacherAddSubject(
        @Path("id") id: Int,
        @Query("subjectId") subjectId:Int
    ): Response<Unit?>
}