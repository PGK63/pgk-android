package ru.pgk63.feature_group.screens.createGroupScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.department.repository.DepartmentRepository
import ru.pgk63.core_model.group.CreateGroupBody
import ru.pgk63.core_model.group.CreateGroupResponse
import ru.pgk63.core_common.api.group.repository.GroupRepository
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.speciality.repository.SpecializationRepository
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val departmentRepository: DepartmentRepository,
    private val specialityRepository: SpecializationRepository,
    private val teacherRepository: TeacherRepository
): ViewModel() {

    private val _responseDepartmentList = MutableStateFlow<PagingData<ru.pgk63.core_model.department.Department>>(PagingData.empty())
    val responseDepartmentList = _responseDepartmentList.asStateFlow()

    private val _responseSpecialityList = MutableStateFlow<PagingData<Specialization>>(PagingData.empty())
    val responseSpecialityList = _responseSpecialityList.asStateFlow()

    private val _responseTeacherList = MutableStateFlow<PagingData<Teacher>>(PagingData.empty())
    val responseTeacherList = _responseTeacherList.asStateFlow()

    private val _responseCreateGroupResult = MutableStateFlow<Result<CreateGroupResponse>?>(null)
    val responseCreateGroupResult = _responseCreateGroupResult.asStateFlow()

    fun createGroup(body: CreateGroupBody) {
        viewModelScope.launch {
            _responseCreateGroupResult.value = groupRepository.create(body)
        }
    }

    fun getDepartmentList(search: String? = null){
        viewModelScope.launch {
            departmentRepository.getAll(
                search = search
            ).cachedIn(viewModelScope).collect {
                _responseDepartmentList.value = it
            }
        }
    }

    fun getSpecialityList(search: String? = null, departmentId: Int? = null){
        viewModelScope.launch {
            specialityRepository.getAll(
                search = search,
                departmentIds = if(departmentId == null) listOf() else listOf(departmentId)
            ).cachedIn(viewModelScope).collect {
                _responseSpecialityList.value = it
            }
        }
    }

    fun getTeacherList(search: String? = null){
        viewModelScope.launch {
            teacherRepository.getAll(
                search = search
            ).cachedIn(viewModelScope).collect {
                _responseTeacherList.value = it
            }
        }
    }

    fun responseCreateGroupToNull() {
        _responseCreateGroupResult.value = null
    }
}