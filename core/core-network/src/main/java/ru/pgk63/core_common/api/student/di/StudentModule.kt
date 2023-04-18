package ru.pgk63.core_common.api.student.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.student.StudentApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StudentModule {

    @[Provides Singleton]
    fun providerStudentApi(
        retrofit: Retrofit
    ): StudentApi = retrofit.create()
}