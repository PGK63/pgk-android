package ru.lfybkf19.feature_journal.screens.journalSubjectListScreen.viewModel

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
import ru.pgk63.core_common.api.journal.repository.JournalRepository
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_model.journal.JournalSubject
import javax.inject.Inject

@HiltViewModel
internal class JournalSubjectListViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val userDataSource: UserDataSource
): ViewModel() {

    private val _responseJournalSubjectList = MutableStateFlow<PagingData<JournalSubject>>(PagingData.empty())
    val responseJournalSubjectList = _responseJournalSubjectList.asStateFlow()

    val responseUserLocal = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun getJournalSubjectAll(journalId: Int) {
        viewModelScope.launch {
            journalRepository.getJournalSubjects(journalId)
                .cachedIn(viewModelScope).collect {
                    _responseJournalSubjectList.value = it
                }
        }
    }
}