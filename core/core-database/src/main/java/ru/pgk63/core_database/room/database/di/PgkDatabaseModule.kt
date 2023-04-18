package ru.pgk63.core_database.room.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.pgk63.core_database.room.database.PgkDatabase
import ru.pgk63.core_database.room.database.history.dao.HistoryDao
import ru.pgk63.core_database.room.database.historySorting.dao.HistorySortingDao
import ru.pgk63.core_database.room.database.journal.dao.JournalDao
import ru.pgk63.core_database.room.database.system.dao.SystemDao
import ru.pgk63.core_database.room.database.utils.ConstantsDatabase.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class PgkDatabaseModule {

    @[Provides Singleton]
    fun providerDatabase(
        @ApplicationContext context: Context
    ): PgkDatabase = Room.databaseBuilder(
        context.applicationContext,
        PgkDatabase::class.java,
        DATABASE_NAME
    ).build()

    @[Provides Singleton]
    fun providerHistoryDao(database: PgkDatabase): HistoryDao = database.historyDao()

    @[Provides Singleton]
    fun providerHistorySortingDao(database: PgkDatabase): HistorySortingDao = database.historySortingDao()

    @[Provides Singleton]
    fun providerSystemDao(database: PgkDatabase): SystemDao = database.systemDao()

    @[Provides Singleton]
    fun providerJournalDao(database: PgkDatabase): JournalDao = database.journalDao()
}