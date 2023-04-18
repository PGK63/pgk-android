package ru.pgk63.core_database.room.database.history.dataSource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_database.room.database.history.dao.HistoryDao
import ru.pgk63.core_database.room.database.history.model.History
import javax.inject.Inject

class HistoryDataSource @Inject constructor(
    private val dao: HistoryDao
) {
    val count = dao.getCount()

    suspend fun add(history: History){

        dao.removeOldData()

        val oldHistory = dao.getByContentIdAndType(
            contentId = history.contentId,
            type = history.historyType
        )

        if(oldHistory != null){
            dao.delete(oldHistory)
        }

        dao.add(history)
    }

    fun getAll(): Flow<PagingData<History>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            dao.getAll()
        }.flow
    }

    suspend fun clear() {
        dao.clear()
    }
}