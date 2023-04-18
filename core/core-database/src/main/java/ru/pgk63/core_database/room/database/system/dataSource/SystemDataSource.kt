package ru.pgk63.core_database.room.database.system.dataSource

import ru.pgk63.core_database.room.database.system.dao.SystemDao
import ru.pgk63.core_database.room.database.system.model.SystemDatabaseSizeInfo
import javax.inject.Inject

class SystemDataSource @Inject constructor(
    private val systemDao: SystemDao
) {
    suspend fun getDatabaseSizeInfo() : SystemDatabaseSizeInfo {
        return systemDao.getDatabaseSizeInfo()
    }
}