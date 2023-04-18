package ru.pgk63.core_common.api.department

import retrofit2.Response
import retrofit2.http.*
import ru.pgk63.core_common.Constants

interface DepartmentApi {

    @GET("/pgk63/api/Department")
    suspend fun getAll(
        @Query("search") search:String? = null,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = Constants.PAGE_SIZE
    ): ru.pgk63.core_model.department.DepartmentResponse

    @GET("/pgk63/api/Department/{id}")
    suspend fun getById(@Path("id") id:Int): Response<ru.pgk63.core_model.department.Department>

    @POST("/pgk63/api/Department")
    suspend fun create(@Body body: ru.pgk63.core_model.department.CreateDepartmentBody): Response<ru.pgk63.core_model.department.Department>

    @PUT("/pgk63/api/Department/{id}")
    suspend fun update(@Path("id") id: Int, @Body body: ru.pgk63.core_model.department.UpdateDepartmentBody): Response<Unit?>

    @DELETE("/pgk63/api/Department")
    suspend fun delete(@Path("id") id: Int): Response<Unit?>
}