package ru.pgk63.core_database.room.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.pgk63.core_database.room.database.converter.DateConverter
import ru.pgk63.core_database.room.database.history.dao.HistoryDao
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.historySorting.dao.HistorySortingDao
import ru.pgk63.core_database.room.database.historySorting.model.HistorySorting
import ru.pgk63.core_database.room.database.journal.dao.JournalDao
import ru.pgk63.core_database.room.database.journal.model.JournalEntity
import ru.pgk63.core_database.room.database.migrations.MigrationFrom1To2
import ru.pgk63.core_database.room.database.system.dao.SystemDao

@Database(
    entities = [History::class, HistorySorting::class, JournalEntity::class],
    version = 5,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = MigrationFrom1To2::class
        ),
        AutoMigration(
            from = 2,
            to = 3
        ),
        AutoMigration(
            from = 3,
            to = 4
        ),
        AutoMigration(
            from = 4,
            to = 5
        ),
    ]
)
@TypeConverters(value = [DateConverter::class])
internal abstract class PgkDatabase: RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    abstract fun historySortingDao(): HistorySortingDao

    abstract fun systemDao(): SystemDao

    abstract fun journalDao(): JournalDao
}