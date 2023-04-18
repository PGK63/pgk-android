package ru.pgk63.core_common.api.journal

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.pgk63.core_common.Constants.JOURNAL_SUBJECTS_PAGE_SIZE
import ru.pgk63.core_common.Constants.PAGE_SIZE

interface JournalApi {

    @GET("/pgk63/api/Journal")
    suspend fun getAll(
        @Query("course") course:List<Int>?,
        @Query("semesters") semesters:List<Int>?,
        @Query("groupIds") groupIds:List<Int>?,
        @Query("specialityIds") specialityIds:List<Int>?,
        @Query("departmentIds") departmentIds:List<Int>?,
        @Query("pageNumber") pageNumber:Int,
        @Query("pageSize") pageSize:Int = PAGE_SIZE
    ): ru.pgk63.core_model.journal.JournalResponse

    @POST("/pgk63/api/Journal")
    suspend fun create(
        @Body body: ru.pgk63.core_model.journal.CreateJournalBody
    ): Response<ru.pgk63.core_model.journal.Journal?>

    @DELETE("/pgk63/api/Journal/{id}")
    suspend fun delete(
        @Path("id") id:Int
    ): Response<Unit?>

    @GET("/pgk63/api/Journal/Subject")
    suspend fun getJournalSubjects(
        @Query("journalId") journalId:Int?,
        @Query("pageNumber") pageNumber:Int,
        @Query("pageSize") pageSize:Int = JOURNAL_SUBJECTS_PAGE_SIZE
    ): ru.pgk63.core_model.journal.JournalSubjectResponse

    @POST("/pgk63/api/Journal/{id}/Subject")
    suspend fun createJournalSubject(
        @Path("id") journalId: Int,
        @Body body: ru.pgk63.core_model.journal.CreateJournalSubjectBody
    ): Response<Unit?>

    @DELETE("/pgk63/api/Journal/Subject/{id}")
    suspend fun deleteJournalSubject(
        @Path("id") journalSubjectId: Int,
    ): Response<Unit?>

    @GET("/pgk63/api/Journal/Subject/Topic")
    suspend fun getJournalTopic(
        @Query("journalSubjectId") journalSubjectId:Int?,
        @Query("pageNumber") pageNumber:Int,
        @Query("pageSize") pageSize:Int = PAGE_SIZE
    ): ru.pgk63.core_model.journal.JournalTopicResponse

    @POST("/pgk63/api/Journal/Subject/{id}/Topic")
    suspend fun createJournalTopic(
        @Path("id") journalSubjectId: Int,
        @Body body: ru.pgk63.core_model.journal.CreateJournalTopicBody
    ): Response<Unit?>

    @DELETE("/pgk63/api/Journal/Subject/Topic/{id}")
    suspend fun deleteJournalTopic(
        @Path("id") journalTopicId: Int,
    ): Response<Unit?>

    @GET("/pgk63/api/Journal/Subject/Row/Column")
    suspend fun getJournalColumn(
        @Query("journalRowId") journalRowId:Int?,
        @Query("studentIds") studentIds:List<Int>?,
        @Query("evaluation") evaluation: ru.pgk63.core_model.journal.JournalEvaluation?,
        @Query("pageNumber") pageNumber:Int,
        @Query("pageSize") pageSize:Int = PAGE_SIZE
    ): ru.pgk63.core_model.journal.JournalColumnResponse

    @GET("/pgk63/api/Journal/Subject/Row")
    suspend fun getJournalRow(
        @Query("journalSubjectId") journalSubjectId:Int?,
        @Query("studentIds") studentIds:List<Int>?,
        @Query("evaluation") evaluation: ru.pgk63.core_model.journal.JournalEvaluation?,
        @Query("pageNumber") pageNumber:Int,
        @Query("pageSize") pageSize:Int = PAGE_SIZE
    ): ru.pgk63.core_model.journal.JournalRowResponse

    @POST("/pgk63/api/Journal/Subject/Row/Column")
    suspend fun createColumn(@Body body: ru.pgk63.core_model.journal.CreateJournalColumnBody): Response<Unit?>

    @PATCH("/pgk63/api/Journal/Subject/Row/Column/{id}/Evaluation")
    suspend fun updateEvaluation(
        @Path("id") columnId: Int,
        @Query("evaluation") evaluation: ru.pgk63.core_model.journal.JournalEvaluation
    ): Response<Unit?>

    @DELETE("/pgk63/api/Journal/Subject/Row/Column/{id}")
    suspend fun deleteColumn(
        @Path("id") columnId: Int,
    ): Response<Unit?>
}