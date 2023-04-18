package ru.pgk63.pgk.screens.updateAppScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import ru.firstproject.core_services.remoteConfig.model.LastVersionApp
import ru.firstproject.core_services.remoteConfig.RemoteConfigService
import javax.inject.Inject

@HiltViewModel
class UpdateAppViewModel @Inject constructor(
    private val remoteConfigService: RemoteConfigService
): ViewModel() {

    private val _lastVersionApp = MutableStateFlow<LastVersionApp?>(null)
    val lastVersionApp = _lastVersionApp.filterNotNull()

    fun getAppVersion() {
       try {
            val lastVersionApp = remoteConfigService.getLastVersionApp()
           _lastVersionApp.value = lastVersionApp
       }catch (_:Exception) {

       }
    }
}