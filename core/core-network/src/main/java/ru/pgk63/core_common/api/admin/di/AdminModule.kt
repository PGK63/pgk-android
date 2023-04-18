package ru.pgk63.core_common.api.admin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.admin.AdminApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AdminModule {

    @[Provides Singleton]
    fun providerAdminApi(retrofit: Retrofit):AdminApi = retrofit.create()
}