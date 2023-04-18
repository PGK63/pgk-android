package ru.pgk63.core_common.api.department.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.department.DepartmentApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DepartmentModule {

    @[Provides Singleton]
    fun providerDepartment(
        retrofit: Retrofit
    ): DepartmentApi = retrofit.create()
}