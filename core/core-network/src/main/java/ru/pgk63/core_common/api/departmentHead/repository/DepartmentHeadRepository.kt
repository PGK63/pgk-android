package ru.pgk63.core_common.api.departmentHead.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.departmentHead.DepartmentHeadApi
import ru.pgk63.core_model.departmentHead.DepartmentHead
import ru.pgk63.core_common.api.departmentHead.paging.DepartmentHeadPageSourse
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.history.model.HistoryType
import javax.inject.Inject

class DepartmentHeadRepository @Inject constructor(
    private val departmentHeadApi: DepartmentHeadApi,
    private val historyDataSource: HistoryDataSource
): ApiResponse() {

    suspend fun registration(body: UserRegistrationBody) = safeApiCall {
        departmentHeadApi.registration(body)
    }

    fun getAll(search:String? = null): Flow<PagingData<DepartmentHead>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            DepartmentHeadPageSourse(
                departmentHeadApi = departmentHeadApi,
                search = search
            )
        }.flow
    }

    suspend fun getById(id: Int): Result<DepartmentHead> {
        val response = safeApiCall { departmentHeadApi.getById(id) }

        response.data?.let { departmentHead ->
            historyDataSource.add(
                History(
                    contentId = departmentHead.id,
                    title = departmentHead.fioAbbreviated(),
                    historyType = HistoryType.DEPARTMENT_HEAD
                )
            )
        }

        return response
    }
}