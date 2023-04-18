package ru.lfybkf19.feature_journal.screens.journalTopicTableScreen.viewModel

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
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class JournalTopicTableViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val userDataSource: UserDataSource
): ViewModel() {

    private val _responseJournalTopicList = MutableStateFlow<PagingData<ru.pgk63.core_model.journal.JournalTopic>>(PagingData.empty())
    val responseJournalTopicList = _responseJournalTopicList.asStateFlow()

    private val _responseCreateJournalTopic = MutableStateFlow<Result<Unit?>?>(null)
    val responseCreateJournalTopic = _responseCreateJournalTopic.asStateFlow()

    private val _responseDeleteJournalTopic = MutableStateFlow<Result<Unit?>?>(null)
    val responseDeleteJournalTopic = _responseDeleteJournalTopic.asStateFlow()

    val responseLocalUser = userDataSource.get().stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun getJournalTopics(journalSubjectId: Int) {
        viewModelScope.launch {
            journalRepository.getJournalTopics(
                journalSubjectId = journalSubjectId
            ).cachedIn(viewModelScope).collect {
                _responseJournalTopicList.value = it
            }
        }
    }

    fun createJournalTopic(
        journalSubjectId: Int,
        body: ru.pgk63.core_model.journal.CreateJournalTopicBody
    ) {
        viewModelScope.launch {
            _responseCreateJournalTopic.value = journalRepository.createJournalTopic(
                journalSubjectId = journalSubjectId,
                body = body
            )
        }
    }

    fun deleteJournalTopic(id: Int) {
        viewModelScope.launch {
            _responseDeleteJournalTopic.value = journalRepository.deleteJournalTopic(id)
        }
    }
}