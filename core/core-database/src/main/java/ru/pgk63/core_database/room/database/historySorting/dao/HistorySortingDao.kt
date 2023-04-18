package ru.pgk63.core_database.room.database.historySorting.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ru.pgk63.core_database.room.database.historySorting.model.HistorySorting
import ru.pgk63.core_database.room.database.historySorting.model.HistorySortingType
import ru.pgk63.core_database.room.database.utils.ConstantsDatabase

@Dao
interface HistorySortingDao {

    @Upsert
    suspend fun add(history: HistorySorting)

    @Query("SELECT * FROM user_history_sorting ORDER BY date DESC")
    fun getAll(): PagingSource<Int, HistorySorting>

    @Query("SELECT * FROM user_history_sorting WHERE contentId = :contentId AND type = :type")
    suspend fun getByContentIdAndType(contentId: Int, type: HistorySortingType): HistorySorting?

    @Delete
    suspend fun delete(history: HistorySorting)

    @Query("DELETE FROM user_history_sorting WHERE id IN (SELECT id FROM user_history_sorting ORDER BY id DESC LIMIT 1 OFFSET :limit)")
    suspend fun removeOldData(limit: Int = ConstantsDatabase.HISTORY_LIMIT_COLUMN_DATABASE)
}