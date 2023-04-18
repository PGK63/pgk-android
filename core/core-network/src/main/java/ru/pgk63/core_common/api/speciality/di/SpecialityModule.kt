package ru.pgk63.core_common.api.speciality.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.pgk63.core_common.api.speciality.SpecializationApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SpecialityModule {

    @[Provides Singleton]
    fun providerSpeciality(
        retrofit: Retrofit
    ): SpecializationApi = retrofit.create()
}