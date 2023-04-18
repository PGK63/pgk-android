package ru.pgk63.core_common.api.group.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.pgk63.core_common.api.group.GroupApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class GroupModule {

    @[Provides Singleton]
    fun providerGroupApi(
        retrofit: Retrofit
    ): GroupApi = retrofit.create(GroupApi::class.java)
}