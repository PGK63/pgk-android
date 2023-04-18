package ru.pgk63.pgk.di

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.pgk63.core_common.di.annotations.VersionCode
import ru.pgk63.core_common.di.annotations.VersionName
import ru.pgk63.pgk.BuildConfig
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @[Provides Singleton VersionCode]
    fun providerVersionCode(): Int = BuildConfig.VERSION_CODE

    @[Provides Singleton VersionName]
    fun providerVersionName(): String = BuildConfig.VERSION_NAME

    @[Provides Singleton]
    fun providerPackageInfo(
        @ApplicationContext context: Context
    ): PackageInfo = context.packageManager.getPackageInfo(
            BuildConfig.APPLICATION_ID,
            PackageManager.GET_PERMISSIONS
        )
}