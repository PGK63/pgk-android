package ru.pgk63.core_common.api.raportichka

import retrofit2.Response
import retrofit2.http.*
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.raportichka.model.RaportichkaAddRowBody
import ru.pgk63.core_common.api.raportichka.model.RaportichkaResponse
import ru.pgk63.core_common.api.raportichka.model.RaportichkaUpdateRowBody

interface RaportichkaApi {

    @GET("/pgk63/api/Raportichka")
    suspend fun getRaportichkaAll(
        @Query("confirmation") confirmation:Boolean? = null,
        @Query("onlyDate") onlyDate:String? = null,
        @Query("startDate") startDate:String? = null,
        @Query("endDate") endDate:String? = null,
        @Query("groupIds") groupIds:List<Int>? = null,
        @Query("subjectIds") subjectIds:List<Int>? = null,
        @Query("classroomTeacherIds") classroomTeacherIds:List<Int>? = null,
        @Query("numberLessons") numberLessons:List<Int>? = null,
        @Query("teacherIds") teacherIds:List<Int>? = null,
        @Query("studentIds") studentIds:List<Int>? = null,
        @Query("pageNumber") pageNumber:Int? = null,
        @Query("pageSize") pageSize:Int = PAGE_SIZE
    ): RaportichkaResponse

    @POST("/pgk63/api/Raportichka/{id}/Row")
    suspend fun raportichkaAddRow(
        @Path("id") raportichkaId:Int,
        @Body body: RaportichkaAddRowBody
    ): Response<Unit?>

    @PUT("/pgk63/api/Raportichka/Row/{id}")
    suspend fun updateRow(
        @Path("id") rowId:Int,
        @Body body: RaportichkaUpdateRowBody
    ): Response<Unit?>

    @PATCH("/pgk63/api/Raportichka/Row/{id}/Confirmation")
    suspend fun updateConfirmation(
        @Path("id") rowId:Int
    ): Response<Unit?>

    @DELETE("/pgk63/api/Raportichka/Row/{id}")
    suspend fun deleteRow(@Path("id") id:Int): Response<Unit?>
}