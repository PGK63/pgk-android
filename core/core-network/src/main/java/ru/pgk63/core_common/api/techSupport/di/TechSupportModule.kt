package ru.pgk63.core_common.api.techSupport.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.pgk63.core_common.api.techSupport.TechSupportApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TechSupportModule {

    @[Provides Singleton]
    fun providerTechSupportApi(
        retrofit: Retrofit
    ): TechSupportApi = retrofit.create(TechSupportApi::class.java)
}