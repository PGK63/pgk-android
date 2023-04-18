package ru.pgk63.core_common.api.journal.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.journal.JournalApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class JournalModule {

    @[Provides Singleton]
    fun providerJournalApi(
        retrofit: Retrofit
    ): JournalApi = retrofit.create()
}