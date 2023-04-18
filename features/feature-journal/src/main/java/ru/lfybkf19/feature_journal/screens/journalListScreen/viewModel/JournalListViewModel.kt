package ru.lfybkf19.feature_journal.screens.journalListScreen.viewModel

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
import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.department.repository.DepartmentRepository
import ru.pgk63.core_common.api.group.repository.GroupRepository
import ru.pgk63.core_model.journal.Journal
import ru.pgk63.core_common.api.journal.repository.JournalRepository
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.speciality.repository.SpecializationRepository
import ru.pgk63.core_database.room.database.journal.dataSource.JournalDataSource
import javax.inject.Inject

@HiltViewModel
internal class JournalListViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val groupRepository: GroupRepository,
    private val departmentRepository: DepartmentRepository,
    private val specializationRepository: SpecializationRepository,
    journalDataSource: JournalDataSource
): ViewModel() {

    private val _responseJournalList = MutableStateFlow<PagingData<Journal>>(PagingData.empty())
    val responseJournalList = _responseJournalList.asStateFlow()

    private val _responseGroupList = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    val responseGroupList = _responseGroupList.asStateFlow()

    private val _responseDepartmentList = MutableStateFlow<PagingData<Department>>(PagingData.empty())
    val responseDepartmentList = _responseDepartmentList.asStateFlow()

    private val _responseSpecializationList = MutableStateFlow<PagingData<Specialization>>(PagingData.empty())
    val responseSpecializationList = _responseSpecializationList.asStateFlow()

    val journalListDownload = journalDataSource.journalList
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Eagerly, PagingData.empty())

    fun getJournalList(
        course:List<Int>? = null,
        semesters:List<Int>? = null,
        groupIds:List<Int>? = null,
        specialityIds:List<Int>? = null,
        departmentIds:List<Int>? = null
    ){
        viewModelScope.launch {
            journalRepository.getAll(
                course = course,
                semesters = semesters,
                groupIds = groupIds,
                specialityIds = specialityIds,
                departmentIds = departmentIds
            ).cachedIn(viewModelScope).collect {
                _responseJournalList.value = it
            }
        }
    }

    fun getGroupList(search: String?) {
        viewModelScope.launch {
            groupRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseGroupList.value = it
            }
        }
    }

    fun getDepartmentList(search: String?) {
        viewModelScope.launch {
            departmentRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseDepartmentList.value = it
            }
        }
    }

    fun getSpecializationList(search: String?) {
        viewModelScope.launch {
            specializationRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseSpecializationList.value = it
            }
        }
    }
}