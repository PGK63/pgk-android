package ru.pgk63.feature_main.screens.mainScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.journal.repository.JournalRepository
import ru.pgk63.core_common.api.raportichka.model.Raportichka
import ru.pgk63.core_common.api.raportichka.repository.RaportichkaRepository
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
    private val raportichkaRepository: RaportichkaRepository,
    private val journalRepository: JournalRepository,
    private val historyDataSource: HistoryDataSource
): ViewModel() {

    private val _responseUserNetwork = MutableStateFlow<Result<UserDetails>>(Result.Loading())
    val responseUserNetwork = _responseUserNetwork.asStateFlow()

    private val _responseRaportichkaList = MutableStateFlow<PagingData<Raportichka>>(PagingData.empty())
    val responseRaportichkaList = _responseRaportichkaList.asStateFlow()

    private val _responseJournalColumnList = MutableStateFlow<PagingData<ru.pgk63.core_model.journal.JournalColumn>>(PagingData.empty())
    val responseJournalColumnList = _responseJournalColumnList.asStateFlow()

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

    fun getRaportichkaList(studentIds:List<Int>? = listOf()) {
        viewModelScope.launch {
            raportichkaRepository.getRaportichkaAll(
                studentIds = studentIds
            ).cachedIn(viewModelScope).collect {
                    _responseRaportichkaList.value = it
                }
        }
    }

    fun getJournalColumnList(studentIds:List<Int>? = listOf()) {
        viewModelScope.launch {
            journalRepository.getJournalColumn(
                studentIds = studentIds
            ).cachedIn(viewModelScope).collect {
                _responseJournalColumnList.value = it
            }
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