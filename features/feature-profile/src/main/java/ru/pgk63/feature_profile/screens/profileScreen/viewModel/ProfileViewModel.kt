package ru.pgk63.feature_profile.screens.profileScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.user.repository.UserRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.user.UserDataSource
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    userDataSource: UserDataSource
): ViewModel() {

    val responseUser = flow {
        emit(userRepository.get())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Result.Loading())

    val userLocalDatabase = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _responseUpdateUserImageUrl = MutableStateFlow<String?>(null)
    val responseUpdateUserImageUrl = _responseUpdateUserImageUrl.asStateFlow()

    fun uploadUserImage(file: ByteArray){
        viewModelScope.launch {
            val result = userRepository.uploadImage(file)

            _responseUpdateUserImageUrl.value = result.data?.url
        }
    }
}