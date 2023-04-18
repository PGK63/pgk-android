package ru.pgk63.core_common.api.subject.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.subject.SubjectApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SubjectModule {

    @[Provides Singleton]
    fun providerSubjectApi(
        retrofit: Retrofit
    ): SubjectApi = retrofit.create()
}