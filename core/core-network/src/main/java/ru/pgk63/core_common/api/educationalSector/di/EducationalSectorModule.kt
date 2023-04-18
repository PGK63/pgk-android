package ru.pgk63.core_common.api.educationalSector.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.educationalSector.EducationalSectorApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class EducationalSectorModule {

    @[Provides Singleton]
    fun providerEducationalSectorApi(retrofit: Retrofit): EducationalSectorApi = retrofit.create()
}