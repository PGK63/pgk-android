package ru.pgk63.feature_settings.screens.aboutAppScreen.viewModel

import android.content.pm.PackageInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.firstproject.core_services.remoteConfig.RemoteConfigService
import ru.pgk63.core_common.di.annotations.VersionCode
import ru.pgk63.core_common.di.annotations.VersionName
import ru.pgk63.feature_settings.screens.aboutAppScreen.model.CurrentVersionApp
import java.util.*
import javax.inject.Inject

@HiltViewModel
internal class AboutAppViewModel @Inject constructor(
    remoteConfigService: RemoteConfigService,
    @VersionCode private val versionCode: Int,
    @VersionName private val versionName: String,
    packageInfo: PackageInfo
): ViewModel() {

    val lastVersionApp by mutableStateOf(remoteConfigService.getLastVersionApp())

    val currentVersionApp by mutableStateOf(
        CurrentVersionApp(
            installDate = Date(packageInfo.firstInstallTime),
            updateDate = Date(packageInfo.lastUpdateTime ),
            versionCode = versionCode,
            versionName = versionName
        )
    )
}