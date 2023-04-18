package ru.pgk63.core_common.api.headman.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.headman.HeadmanApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class HeadmanModule {

    @[Provides Singleton]
    fun providerHeadmanApi(
        retrofit: Retrofit
    ): HeadmanApi = retrofit.create()
}