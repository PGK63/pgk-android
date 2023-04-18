package ru.pgk63.feature_settings.screens.changePasswordScreen.viewModel

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
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _responseNewPassword = MutableStateFlow<Result<String>?>(null)
    val responseNewPassword = _responseNewPassword.asStateFlow()

    fun updatePassword() {
        viewModelScope.launch {
            _responseNewPassword.value = userRepository.updatePassword()
        }
    }
}