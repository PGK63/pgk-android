package ru.pgk63.core_common.api.departmentHead.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.departmentHead.DepartmentHeadApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DepartmentHeadModule {

    @[Provides Singleton]
    fun providerDepartmentHeadApi(retrofit: Retrofit): DepartmentHeadApi = retrofit.create()
}