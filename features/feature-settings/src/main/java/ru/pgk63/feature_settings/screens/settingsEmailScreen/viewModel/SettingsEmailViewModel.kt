package ru.pgk63.feature_settings.screens.settingsEmailScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.user.model.UserDetails
import ru.pgk63.core_common.api.user.repository.UserRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class SettingsEmailViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _responseUserResult = MutableStateFlow<Result<UserDetails>>(Result.Loading())
    val responseUserResult = _responseUserResult.asStateFlow()

    private val _responseSendEmailResult = MutableStateFlow<Result<Unit?>?>(null)
    val responseSendEmailResult = _responseSendEmailResult.asStateFlow()

    fun getUser() {
        viewModelScope.launch {
            _responseUserResult.value = userRepository.get()
        }
    }

    fun emailVerification() {
        viewModelScope.launch {
            _responseSendEmailResult.value = userRepository.emailVerification()
        }
    }

    fun updateEmail(email: String) {
        viewModelScope.launch {
            _responseSendEmailResult.value = userRepository.updateEmail(email)
        }
    }

    fun responseSendEmailResultToNull() {
        _responseSendEmailResult.value = null
    }
}