package ru.lfybkf19.feature_journal.screens.createJournalSubject.viewModel

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
import ru.pgk63.core_common.api.subject.repository.SubjectRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class CreateJournalSubjectViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val subjectRepository: SubjectRepository,
    userDataSource: UserDataSource
): ViewModel() {

    private val _responseCreateJournalSubjectResult = MutableStateFlow<Result<Unit?>?>(null)
    val responseCreateJournalSubjectResult = _responseCreateJournalSubjectResult.asStateFlow()

    private val _responseSubjectList = MutableStateFlow<PagingData<ru.pgk63.core_model.subject.Subject>>(PagingData.empty())
    val responseSubjectList = _responseSubjectList.asStateFlow()

    val responseUserLocal = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun createJournalSubject(journalId: Int, body: ru.pgk63.core_model.journal.CreateJournalSubjectBody) {
        viewModelScope.launch {
            val response = journalRepository.createJournalSubject(journalId, body)
            _responseCreateJournalSubjectResult.value = response
        }
    }

    fun getSubjectList(search: String? = null, teacherId: Int?) {
        viewModelScope.launch {
            subjectRepository.getAll(
                search = search,
                teacherIds = if(teacherId != null) listOf(teacherId) else null
            ).cachedIn(viewModelScope).collect {
                _responseSubjectList.value = it
            }
        }
    }
}