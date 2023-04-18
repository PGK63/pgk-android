package ru.pgk63.core_common.api.student.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.student.StudentApi
import ru.pgk63.core_common.api.student.paging.StudentPagingSource
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.history.model.HistoryType
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val studentApi: StudentApi,
    private val historyDataSource: HistoryDataSource
): ApiResponse() {

    suspend fun registration(body: ru.pgk63.core_model.student.StudentRegistrationBody) = safeApiCall {
        studentApi.registration(body)
    }

    fun getAll(
        search:String? = null,
        pageSize: Int = Constants.PAGE_SIZE,
        groupIds:List<Int>? = null
    ): Flow<PagingData<ru.pgk63.core_model.student.Student>> {
        return Pager(PagingConfig(pageSize = pageSize)){
            StudentPagingSource(
                studentApi = studentApi,
                search = search,
                groupIds = groupIds,
                pageSize = pageSize
            )
        }.flow
    }

    suspend fun getById(id:Int): Result<ru.pgk63.core_model.student.Student> {
        val response = safeApiCall { studentApi.getById(id) }

        response.data?.let { student ->
            historyDataSource.add(
                History(
                    contentId = student.id,
                    title = student.fioAbbreviated(),
                    description = student.group.toString(),
                    historyType = HistoryType.STUDENT
                )
            )
        }

        return response
    }
}