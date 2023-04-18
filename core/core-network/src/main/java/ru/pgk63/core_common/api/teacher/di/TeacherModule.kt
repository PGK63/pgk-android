package ru.pgk63.core_common.api.teacher.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.teacher.TeacherApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TeacherModule {

    @[Provides Singleton]
    fun providerTeacherApi(
        retrofit: Retrofit
    ): TeacherApi = retrofit.create()
}