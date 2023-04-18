package ru.pgk63.feature_subject.screens.subjectListScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.subject.repository.SubjectRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class SubjectListViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    userDataSource: UserDataSource
): ViewModel() {

    private val _responseSubject = MutableStateFlow<PagingData<ru.pgk63.core_model.subject.Subject>>(PagingData.empty())
    val responseSubject = _responseSubject.asStateFlow()

    private val _responseCreateSubjectResult = MutableStateFlow<Result<ru.pgk63.core_model.subject.CreateSubjectResponse>?>(null)
    val responseCreateSubjectResult = _responseCreateSubjectResult.asStateFlow()

    val responseLocalUser = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun getSubjectAll(search: String? = null) {
        viewModelScope.launch {
            subjectRepository.getAll(search).cachedIn(viewModelScope).collect {
                _responseSubject.value = it
            }
        }
    }

    fun createSubject(body: ru.pgk63.core_model.subject.CreateSubjectBody) {
        viewModelScope.launch {
            _responseCreateSubjectResult.value = subjectRepository.create(body)
        }
    }

    fun createSubjectResultTNull() {
        viewModelScope.launch {
            _responseCreateSubjectResult.value = null
        }
    }
}