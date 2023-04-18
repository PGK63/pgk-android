package ru.pgk63.core_common.api.raportichka.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.raportichka.RaportichkaApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RaportichkaModule {

    @[Provides Singleton]
    fun providerRaportichkaApi(
        retrofit: Retrofit
    ): RaportichkaApi = retrofit.create()
}