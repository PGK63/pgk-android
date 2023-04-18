package ru.pgk63.core_common.api.group

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_model.group.GroupResponse
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.group.model.CreateGroupBody
import ru.pgk63.core_common.api.group.model.CreateGroupResponse

interface GroupApi {

    @GET("/pgk63/api/Group")
    suspend fun getAll(
        @Query("search") search:String? = null,
        @Query("course") course: List<Int>? = null,
        @Query("number") number:List<Int>? = null,
        @Query("specialityIds") specialityIds: List<Int>? = null,
        @Query("departmentIds") departmentIds: List<Int>? = null,
        @Query("сuratorIds") classroomTeacherIds: List<Int>? = null,
        @Query("deputyHeadmaIds") deputyHeadmaIds: List<Int>? = null,
        @Query("headmanIds") headmanIds: List<Int>? = null,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = PAGE_SIZE
    ): GroupResponse

    @POST("/pgk63/api/Group")
    suspend fun create(
        @Body body: CreateGroupBody
    ): Response<CreateGroupResponse>

    @DELETE("/pgk63/api/Group/{id}")
    suspend fun deleteById(
        @Path("id") id: Int
    ): Response<Unit?>

    @GET("/pgk63/api/Group/{id}")
    suspend fun getById(@Path("id") id: Int): Response<Group>

    @GET("/pgk63/api/Group/{id}/Students")
    suspend fun getStudentByGroupId(
        @Path("id") id: Int,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = PAGE_SIZE
    ): ru.pgk63.core_model.student.StudentResponse

    @POST("/pgk63/api/Group/{id}/Raportichka")
    suspend fun createRaportichka(@Path("id") groupId:Int):Response<Unit?>

}