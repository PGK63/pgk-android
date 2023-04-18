package ru.pgk63.core_common.api.subject.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.subject.SubjectApi
import ru.pgk63.core_common.api.subject.paging.SubjectPagingSource
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.history.model.HistoryType
import javax.inject.Inject

class SubjectRepository @Inject constructor(
    private val subjectApi: SubjectApi,
    private val historyDataSource: HistoryDataSource
): ApiResponse() {

    fun getAll(
        search:String? = null,
        teacherIds:List<Int>? = null
    ): Flow<PagingData<ru.pgk63.core_model.subject.Subject>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            SubjectPagingSource(
                subjectApi = subjectApi,
                search = search,
                teacherIds = teacherIds
            )
        }.flow
    }

    suspend fun getById(id: Int): Result<ru.pgk63.core_model.subject.Subject> {
        val response = safeApiCall { subjectApi.getById(id) }

        response.data?.let { subject ->
            historyDataSource.add(
                History(
                    contentId = subject.id,
                    title = subject.subjectTitle,
                    historyType = HistoryType.SUBJECT
                )
            )
        }

        return response
    }

    suspend fun create(body: ru.pgk63.core_model.subject.CreateSubjectBody) = safeApiCall { subjectApi.create(body) }

    suspend fun teacherAddSubject(teacherId: Int, subjectId: Int): Result<Unit?> {
        return safeApiCall { subjectApi.teacherAddSubject(teacherId, subjectId) }
    }

    suspend fun delete(id: Int) = safeApiCall { subjectApi.delete(id) }

    suspend fun update(id: Int,body: ru.pgk63.core_model.subject.UpdateSubjectBody) = safeApiCall { subjectApi.update(id, body) }
}