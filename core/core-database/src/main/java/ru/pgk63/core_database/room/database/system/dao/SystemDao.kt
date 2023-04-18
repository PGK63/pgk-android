package ru.pgk63.core_database.room.database.system.dao

import androidx.room.Dao
import androidx.room.Query
import ru.pgk63.core_database.room.database.system.model.SystemDatabaseSizeInfo

@Dao
interface SystemDao {

    @Query("SELECT s.*, c.* FROM pragma_page_size as s JOIN pragma_page_count as c")
    suspend fun getDatabaseSizeInfo(): SystemDatabaseSizeInfo
}