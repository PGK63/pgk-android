package ru.pgk63.core_database.room.database.historySorting.dataSource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants
import ru.pgk63.core_database.room.database.historySorting.dao.HistorySortingDao
import ru.pgk63.core_database.room.database.historySorting.model.HistorySorting
import javax.inject.Inject

class HistorySortingDataSource @Inject constructor(
    private val dao: HistorySortingDao
) {
    suspend fun add(history: HistorySorting){

        dao.removeOldData()

        val oldHistory = dao.getByContentIdAndType(
            contentId = history.contentId,
            type = history.type
        )

        if(oldHistory != null){
            dao.delete(oldHistory)
        }

        dao.add(history)
    }

    fun getAll(): Flow<PagingData<HistorySorting>> {
        return Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)){
            dao.getAll()
        }.flow
    }
}