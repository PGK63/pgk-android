package ru.pgk63.core_common.api.department.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.department.DepartmentApi
import ru.pgk63.core_common.api.department.paging.DepartmentPagingSource
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.history.model.HistoryType
import javax.inject.Inject

class DepartmentRepository @Inject constructor(
    private val departmentApi: DepartmentApi,
    private val historyDataSource: HistoryDataSource
): ApiResponse() {

    fun getAll(
        search:String? = null
    ): Flow<PagingData<ru.pgk63.core_model.department.Department>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            DepartmentPagingSource(
                departmentApi = departmentApi,
                search = search
            )
        }.flow
    }

    suspend fun getById(id:Int): Result<ru.pgk63.core_model.department.Department> {
        val response = safeApiCall { departmentApi.getById(id) }

        response.data?.let { department ->
            historyDataSource.add(
                History(
                    contentId = department.id,
                    title = department.name,
                    historyType = HistoryType.DEPARTMENT
                )
            )
        }

        return response
    }

    suspend fun create(body: ru.pgk63.core_model.department.CreateDepartmentBody) = safeApiCall { departmentApi.create(body) }

    suspend fun delete(id: Int) = safeApiCall { departmentApi.delete(id) }

    suspend fun update(id: Int, body: ru.pgk63.core_model.department.UpdateDepartmentBody) = safeApiCall { departmentApi.update(id, body) }
}