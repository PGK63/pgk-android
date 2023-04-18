package ru.pgk63.core_common.api.user.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.pgk63.core_common.api.user.UserApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class UserModule {

    @[Provides Singleton]
    fun providerUserApi(
        retrofit: Retrofit
    ): UserApi = retrofit.create(UserApi::class.java)
}