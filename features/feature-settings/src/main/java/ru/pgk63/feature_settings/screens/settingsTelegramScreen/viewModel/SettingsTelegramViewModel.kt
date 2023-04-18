package ru.pgk63.feature_settings.screens.settingsTelegramScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.user.repository.UserRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class SettingsTelegramViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _responseTelegramToken = MutableStateFlow<Result<String>>(Result.Loading())
    val responseTelegramToken = _responseTelegramToken.asStateFlow()

    fun getTelegramToken() {
        viewModelScope.launch {
            _responseTelegramToken.value = userRepository.getTelegramToken()
        }
    }
}