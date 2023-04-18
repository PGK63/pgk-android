package ru.pgk63.core_common.api.subject

import retrofit2.Response
import retrofit2.http.*
import ru.pgk63.core_common.Constants

interface SubjectApi {

    @GET("/pgk63/api/Subject")
    suspend fun getAll(
        @Query("search") search:String? = null,
        @Query("teacherIds") teacherIds:List<Int>? = null,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = Constants.PAGE_SIZE
    ): ru.pgk63.core_model.subject.SubjectResponse

    @GET("/pgk63/api/Subject/{id}")
    suspend fun getById(@Path("id") id:Int): Response<ru.pgk63.core_model.subject.Subject>

    @POST("/pgk63/api/Subject")
    suspend fun create(@Body body: ru.pgk63.core_model.subject.CreateSubjectBody): Response<ru.pgk63.core_model.subject.CreateSubjectResponse>

    @POST("/pgk63/api/Teacher/{id}/Subject?")
    suspend fun teacherAddSubject(
        @Path("id") teacherId: Int,
        @Query("subjectId") subjectId: Int
    ): Response<Unit?>

    @PUT("/pgk63/api/Subject")
    suspend fun update(
        @Path("id") id: Int,
        @Body body: ru.pgk63.core_model.subject.UpdateSubjectBody,
    ): Response<Unit?>

    @DELETE("/pgk63/api/Subject")
    suspend fun delete(@Path("id") id: Int): Response<Unit?>
}