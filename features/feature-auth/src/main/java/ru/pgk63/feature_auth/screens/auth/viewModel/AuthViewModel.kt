package ru.pgk63.feature_auth.screens.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.auth.model.SignIn
import ru.pgk63.core_common.api.auth.model.SignInResponse
import ru.pgk63.core_common.api.auth.repository.AuthRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _responseSignIn = MutableStateFlow<Result<SignInResponse>?>(null)
    val responseSignIn = _responseSignIn.asStateFlow()

    fun signIn(body: SignIn) {
        viewModelScope.launch {
            try {
                _responseSignIn.value = Result.Loading()

                _responseSignIn.value = authRepository.signIn(body)
            } catch (e: Exception) {
                _responseSignIn.value = Result.Error(e.message ?: "")
            }
        }
    }
}