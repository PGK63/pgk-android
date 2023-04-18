package ru.pgk63.feature_settings.screens.settingsAppearanceScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.user.model.UserSettings
import ru.pgk63.core_common.api.user.repository.UserRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.enums.theme.ThemeCorners
import ru.pgk63.core_common.enums.theme.ThemeFontSize
import ru.pgk63.core_common.enums.theme.ThemeFontStyle
import ru.pgk63.core_common.enums.theme.ThemeStyle
import javax.inject.Inject

@HiltViewModel
internal class SettingsAppearanceViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _responseUserSettings = MutableStateFlow<Result<UserSettings>>(Result.Loading())
    val responseUserSettings = _responseUserSettings.asStateFlow()

    fun getSettings(){
        viewModelScope.launch {
            _responseUserSettings.value = userRepository.getSettings()
        }
    }

    fun updateThemeStyle(themeStyle: ThemeStyle) {
        viewModelScope.launch {
            userRepository.updateThemeStyle(themeStyle)
        }
    }

    fun updateThemeFontStyle(themeFontStyle: ThemeFontStyle) {
        viewModelScope.launch {
            userRepository.updateThemeFontStyle(themeFontStyle)
        }
    }

    fun updateThemeFontSize(themeFontSize: ThemeFontSize) {
        viewModelScope.launch {
            userRepository.updateThemeFontSize(themeFontSize)
        }
    }

    fun updateThemeCorners(themeCorners: ThemeCorners) {
        viewModelScope.launch {
            userRepository.updateThemeCorners(themeCorners)
        }
    }
}