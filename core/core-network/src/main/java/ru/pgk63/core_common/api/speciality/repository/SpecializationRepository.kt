package ru.pgk63.core_common.api.speciality.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.speciality.SpecializationApi
import ru.pgk63.core_common.api.speciality.model.CreateSpecializationBody
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.speciality.paging.SpecializationPagingSource
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.history.model.HistoryType
import javax.inject.Inject

class SpecializationRepository @Inject constructor(
    private val specializationApi: SpecializationApi,
    private val historyDataSource: HistoryDataSource
): ApiResponse() {

    fun getAll(
        search:String? = null,
        departmentIds:List<Int>? = null
    ): Flow<PagingData<Specialization>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            SpecializationPagingSource(
                specializationApi = specializationApi,
                search = search,
                departmentIds = departmentIds
            )
        }.flow
    }

    suspend fun getById(id:Int): Result<Specialization> {
        val response = safeApiCall { specializationApi.getById(id) }

        response.data?.let { specialization ->
            historyDataSource.add(
                History(
                    contentId = specialization.id,
                    title = specialization.nameAbbreviation,
                    historyType = HistoryType.SPECIALITY
                )
            )
        }

        return response
    }

    suspend fun create(body: CreateSpecializationBody) = safeApiCall { specializationApi.create(body) }
}