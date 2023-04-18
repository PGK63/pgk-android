package ru.pgk63.core_common.api.director.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.director.DirectorApi
import ru.pgk63.core_common.api.director.model.Director
import ru.pgk63.core_common.api.director.paging.DirectorPageSourse
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.history.model.HistoryType
import javax.inject.Inject

class DirectorRepository @Inject constructor(
    private val directorApi: DirectorApi,
    private val historyDataSource: HistoryDataSource
): ApiResponse() {

    fun getAll(
        search:String? = null,
        current:Boolean? = null
    ): Flow<PagingData<Director>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            DirectorPageSourse(
                directorApi = directorApi,
                search = search,
                current = current
            )
        }.flow
    }

    suspend fun getById(id: Int): Result<Director> {
        val response = safeApiCall { directorApi.getById(id) }

        response.data?.let { director ->
            historyDataSource.add(
                History(
                    contentId = director.id,
                    title = director.fioAbbreviated(),
                    historyType = HistoryType.DIRECTOR
                )
            )
        }

        return response
    }

    suspend fun registration(body: UserRegistrationBody) = safeApiCall {
        directorApi.registration(body)
    }

    suspend fun updateCurrent(id: Int) = safeApiCall {
        directorApi.updateCurrent(id)
    }
}