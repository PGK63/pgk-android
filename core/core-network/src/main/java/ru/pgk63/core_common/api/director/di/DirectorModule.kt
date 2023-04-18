package ru.pgk63.core_common.api.director.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.director.DirectorApi
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DirectorModule {

    @[Provides Singleton]
    fun providerDirectorApi(retrofit: Retrofit): DirectorApi = retrofit.create()
}