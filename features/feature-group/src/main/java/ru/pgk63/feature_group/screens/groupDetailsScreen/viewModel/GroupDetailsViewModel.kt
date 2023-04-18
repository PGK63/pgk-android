package ru.pgk63.feature_group.screens.groupDetailsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.group.paging.StudentByGroupIdPagingSource
import ru.pgk63.core_common.api.group.repository.GroupRepository
import ru.pgk63.core_model.journal.Journal
import ru.pgk63.core_common.api.journal.repository.JournalRepository
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val journalRepository: JournalRepository,
    private val userDataSource: UserDataSource
): ViewModel() {

    private val _responseGroup = MutableStateFlow<Result<Group>>(Result.Loading())
    val responseGroup = _responseGroup.asStateFlow()

    private val _responseDeleteGroupResult = MutableStateFlow<Result<Unit?>?>(null)
    val responseDeleteGroupResult = _responseDeleteGroupResult.asStateFlow()

    private val _responseJournalList = MutableStateFlow<PagingData<Journal>>(PagingData.empty())
    val responseJournalList = _responseJournalList.asStateFlow()

    val responseLocalUser = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun getGroupById(id: Int){
        viewModelScope.launch {
            val response = groupRepository.getById(id)
            _responseGroup.value = response
        }
    }

    fun getStudentsByGroupId(id: Int): Flow<PagingData<Student>> {
        return Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)){
            StudentByGroupIdPagingSource(
                groupRepository = groupRepository,
                groupId = id
            )
        }.flow.cachedIn(viewModelScope)
    }

    fun getJournalList(groupId: Int) {
        viewModelScope.launch {
            journalRepository.getAll(
                groupIds = listOf(groupId)
            ).cachedIn(viewModelScope).collect {
                _responseJournalList.value = it
            }
        }
    }

    fun deleteGroupById(id: Int){
        viewModelScope.launch {
            _responseDeleteGroupResult.value = groupRepository.deleteById(id)
        }
    }
}