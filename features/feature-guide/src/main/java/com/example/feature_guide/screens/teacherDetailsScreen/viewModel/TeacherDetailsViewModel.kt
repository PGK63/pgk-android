package com.example.feature_guide.screens.teacherDetailsScreen.viewModel

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
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.group.repository.GroupRepository
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_common.api.subject.repository.SubjectRepository
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class TeacherDetailsViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository,
    private val subjectRepository: SubjectRepository,
    private val groupRepository: GroupRepository,
    private val userDataSource: UserDataSource
): ViewModel() {

    private val _responseTeacherDetails = MutableStateFlow<Result<Teacher>>(Result.Loading())
    val responseTeacherDetails = _responseTeacherDetails.asStateFlow()

    private val _responseSubjectList = MutableStateFlow<PagingData<Subject>>(PagingData.empty())
    val responseSubjectList = _responseSubjectList.asStateFlow()

    private val _responseGroupList = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    val responseGroupList = _responseGroupList.asStateFlow()

    val responseUserLocal = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun getTeacherDetails(teacherId: Int) {
        viewModelScope.launch {
            _responseTeacherDetails.value = teacherRepository.getById(teacherId)
        }
    }

    fun getSubjectList(teacherId: Int) {
        viewModelScope.launch {
            subjectRepository.getAll(
                teacherIds = listOf(teacherId)
            ).cachedIn(viewModelScope).collect {
                _responseSubjectList.value = it
            }
        }
    }

    fun getGroupList(teacherId: Int) {
        viewModelScope.launch {
            groupRepository.getAll(
                classroomTeacherIds = listOf(teacherId)
            ).cachedIn(viewModelScope).collect {
                _responseGroupList.value = it
            }
        }
    }
}