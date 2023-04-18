package ru.pgk63.pgk.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.firstproject.core_services.remoteConfig.RemoteConfigService
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.pgk.BuildConfig
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteConfigService: RemoteConfigService,
    userDataSource: UserDataSource
): ViewModel() {

    val user = userDataSource.get().stateIn(viewModelScope, SharingStarted.Eagerly,null)

    val isOldVersionApp = flow {

        try {
            val lastVersionApp = remoteConfigService.getLastVersionApp()

            lastVersionApp?.versionCode?.let {
                emit(lastVersionApp.isOldVersionApp(BuildConfig.VERSION_CODE))
            }
        }catch (e:Exception){
            emit(false)
        }

    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
}