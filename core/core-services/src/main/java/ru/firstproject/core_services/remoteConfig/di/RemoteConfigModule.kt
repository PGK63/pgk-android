package ru.firstproject.core_services.remoteConfig.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class FirebaseModule {

    @[Provides Singleton]
    fun providerRemoteConfig(
        settings: FirebaseRemoteConfigSettings
    ): FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(settings)
    }

    @[Provides Singleton]
    fun providerRemoteConfigSettings(): FirebaseRemoteConfigSettings =
        FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(43200) // 43200 - 12 hour
            .build()
}