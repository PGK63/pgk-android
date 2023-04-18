package ru.pgk63.core_common.api.language.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.language.LanguageApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LanguageModule {

    @[Provides Singleton]
    fun providerLanguageApi(
        retrofit: Retrofit
    ): LanguageApi = retrofit.create()
}