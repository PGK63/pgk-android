package ru.pgk63.feature_auth.screens.forgotPassword.viewModel

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
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _responsePasswordReset = MutableStateFlow<Result<Unit?>?>(null)
    val responsePasswordReset = _responsePasswordReset.asStateFlow()

    fun passwordReset(email: String) {
        viewModelScope.launch {
            _responsePasswordReset.value = userRepository.passwordReset(email)
        }
    }
}