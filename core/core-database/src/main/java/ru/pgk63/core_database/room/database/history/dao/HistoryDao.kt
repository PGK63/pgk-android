package ru.pgk63.core_database.room.database.history.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.history.model.HistoryType
import ru.pgk63.core_database.room.database.utils.ConstantsDatabase.HISTORY_LIMIT_COLUMN_DATABASE

@Dao
interface HistoryDao {

    @Upsert
    suspend fun add(history: History)

    @Query("SELECT * FROM user_history ORDER BY date DESC")
    fun getAll(): PagingSource<Int, History>

    @Query("SELECT * FROM user_history WHERE contentId = :contentId AND historyType = :type")
    suspend fun getByContentIdAndType(contentId: Int, type: HistoryType): History?

    @Delete
    suspend fun delete(history: History)

    @Query("DELETE FROM user_history WHERE historyId IN (SELECT historyId FROM user_history ORDER BY historyId DESC LIMIT 1 OFFSET :limit)")
    suspend fun removeOldData(limit: Int = HISTORY_LIMIT_COLUMN_DATABASE)

    @Query("SELECT COUNT(*) FROM user_history")
    fun getCount(): Flow<Int>

    @Query("DELETE FROM user_history")
    suspend fun clear()
}