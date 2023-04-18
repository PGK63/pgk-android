package ru.lfybkf19.feature_journal.screens.createJournalScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.journal.repository.JournalRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class CreateJournalViewModel @Inject constructor(
    private val journalRepository: JournalRepository
): ViewModel() {

    private val _responseCreateJournalResult = MutableStateFlow<Result<ru.pgk63.core_model.journal.Journal?>?>(null)
    val responseCreateJournalResult = _responseCreateJournalResult.asStateFlow()

    fun createJournal(body: ru.pgk63.core_model.journal.CreateJournalBody) {
        viewModelScope.launch {
            _responseCreateJournalResult.value = journalRepository.create(body)
        }
    }

    fun responseCreateJournalResultToNull() {
        _responseCreateJournalResult.value = null
    }
}