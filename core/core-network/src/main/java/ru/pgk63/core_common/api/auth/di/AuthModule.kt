package ru.pgk63.core_common.api.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.pgk63.core_common.api.auth.AuthApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AuthModule {

    @[Provides Singleton]
    fun providerAuthApi(
        retrofit: Retrofit
    ): AuthApi = retrofit.create(AuthApi::class.java)
}