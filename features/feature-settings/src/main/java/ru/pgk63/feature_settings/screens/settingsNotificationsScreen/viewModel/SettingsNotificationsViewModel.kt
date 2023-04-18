package ru.pgk63.feature_settings.screens.settingsNotificationsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.user.model.NotificationSetting
import ru.pgk63.core_common.api.user.model.UserSettings
import ru.pgk63.core_common.api.user.repository.UserRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
class SettingsNotificationsViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _responseSettings = MutableStateFlow<Result<UserSettings>>(Result.Loading())
    val responseSettings = _responseSettings.asStateFlow()

    fun getSettings() {
        viewModelScope.launch {
            _responseSettings.value = userRepository.getSettings()
        }
    }

    fun updateNotificationSettings(body: NotificationSetting) {
        viewModelScope.launch {
            userRepository.updateNotificationSettings(body)
        }
    }
}