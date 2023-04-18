package ru.pgk63.core_database.room.database.utils

import android.content.Context

class DatabaseUtil {


    fun getRoomDatabaseSize(dbName: String, context: Context): Long {
        val file = context.getDatabasePath(dbName)

        if (!file.exists()) throw Exception("${file.absolutePath} doesn't exist")

        return file.length()
    }
}