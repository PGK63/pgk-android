package ru.pgk63.core_common.api.journal.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants.JOURNAL_SUBJECTS_PAGE_SIZE
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.journal.JournalApi
import ru.pgk63.core_common.api.journal.paging.*
import ru.pgk63.core_common.common.response.ApiResponse
import javax.inject.Inject

class JournalRepository @Inject constructor(
    private val journalApi: JournalApi
): ApiResponse() {

    fun getAll(
        course:List<Int>? = null,
        semesters:List<Int>? = null,
        groupIds:List<Int>? = null,
        specialityIds:List<Int>? = null,
        departmentIds:List<Int>? = null
    ): Flow<PagingData<ru.pgk63.core_model.journal.Journal>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            JournalPagingSource(
                journalApi = journalApi,
                course = course,
                semesters = semesters,
                groupIds = groupIds,
                specialityIds = specialityIds,
                departmentIds = departmentIds,
            )
        }.flow
    }

    suspend fun create(body: ru.pgk63.core_model.journal.CreateJournalBody) = safeApiCall {
        journalApi.create(body)
    }

    fun getJournalSubjects(
        journalId:Int? = null
    ): Flow<PagingData<ru.pgk63.core_model.journal.JournalSubject>> {
        return Pager(PagingConfig(pageSize = JOURNAL_SUBJECTS_PAGE_SIZE)){
            JournalSubjectPagingSource(
                journalApi = journalApi,
                journalId = journalId
            )
        }.flow
    }

    suspend fun createJournalSubject(journalId: Int, body: ru.pgk63.core_model.journal.CreateJournalSubjectBody) = safeApiCall {
        journalApi.createJournalSubject(journalId = journalId, body = body)
    }

    fun getJournalTopics(
        journalSubjectId:Int? = null
    ): Flow<PagingData<ru.pgk63.core_model.journal.JournalTopic>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            JournalTopicPagingSource(
                journalApi = journalApi,
                journalSubjectId = journalSubjectId
            )
        }.flow
    }

    fun getJournalRow(
        journalSubjectId:Int? = null,
        studentIds:List<Int>? = null,
        evaluation: ru.pgk63.core_model.journal.JournalEvaluation? = null
    ): Flow<PagingData<ru.pgk63.core_model.journal.JournalRow>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            JournalRowPagingSource(
                journalApi = journalApi,
                journalSubjectId = journalSubjectId,
                studentIds = studentIds,
                evaluation = evaluation
            )
        }.flow
    }

    fun getJournalColumn(
        journalRowId:Int? = null,
        studentIds:List<Int>? = null,
        evaluation: ru.pgk63.core_model.journal.JournalEvaluation? = null
    ): Flow<PagingData<ru.pgk63.core_model.journal.JournalColumn>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            JournalColumnPagingSourse(
                journalApi = journalApi,
                journalRowId = journalRowId,
                studentIds = studentIds,
                evaluation = evaluation
            )
        }.flow
    }

    suspend fun createColumn(body: ru.pgk63.core_model.journal.CreateJournalColumnBody) = safeApiCall {
        journalApi.createColumn(body)
    }

    suspend fun updateEvaluation(columnId: Int, evaluation: ru.pgk63.core_model.journal.JournalEvaluation) = safeApiCall {
        journalApi.updateEvaluation(columnId, evaluation)
    }

    suspend fun deleteColumn(id: Int) = safeApiCall { journalApi.deleteColumn(id) }

    suspend fun createJournalTopic(
        journalSubjectId: Int,
        body: ru.pgk63.core_model.journal.CreateJournalTopicBody
    ) = safeApiCall {
        journalApi.createJournalTopic(
            journalSubjectId = journalSubjectId,
            body = body
        )
    }

    suspend fun deleteJournalTopic(id: Int) = safeApiCall { journalApi.deleteJournalTopic(id) }
}