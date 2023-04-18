package ru.pgk63.feature_settings.screens.settingsSecurityScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.auth.repository.AuthRepository
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
class SettingsSecurityViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    private val authRepository: AuthRepository
): ViewModel() {

    fun signOutApp() {
        viewModelScope.launch {
            userDataSource.signOut()
        }
    }

    fun signOutAll() {
        viewModelScope.launch {
            val response = authRepository.revokeRefreshToken()
            if(response is Result.Success){
                userDataSource.signOut()
            }
        }
    }
}