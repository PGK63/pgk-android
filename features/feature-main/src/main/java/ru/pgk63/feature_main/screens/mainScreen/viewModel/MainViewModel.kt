package ru.pgk63.feature_main.screens.mainScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.user.model.UserDetails
import ru.pgk63.core_common.api.user.repository.UserRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.user.UserDataSource
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    userDataSource: UserDataSource,
    private val userRepository: UserRepository,
    private val historyDataSource: HistoryDataSource
): ViewModel() {

    private val _responseUserNetwork = MutableStateFlow<Result<UserDetails>>(Result.Loading())
    val responseUserNetwork = _responseUserNetwork.asStateFlow()

    val userLocal = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _responseHistory = MutableStateFlow<PagingData<History>>(PagingData.empty())
    val responseHistory = _responseHistory.asStateFlow()

    fun getUserNetwork(){
        viewModelScope.launch {
            _responseUserNetwork.value = userRepository.get()
        }
    }

    fun updateDarkMode() {
        viewModelScope.launch {
            userRepository.updateDarkMode()
        }
    }

    fun getHistory() {
        viewModelScope.launch {
            historyDataSource.getAll().cachedIn(viewModelScope).collect {
                _responseHistory.value = it
            }
        }
    }
}