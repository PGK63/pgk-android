package ru.pgk63.feature_department.screens.departmentDetailsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.department.repository.DepartmentRepository
import ru.pgk63.core_common.api.group.repository.GroupRepository
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.speciality.repository.SpecializationRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class DepartmentDetailsViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository,
    private val specializationRepository: SpecializationRepository,
    private val groupRepository: GroupRepository
): ViewModel() {

    private val _responseDepartment = MutableStateFlow<Result<ru.pgk63.core_model.department.Department>>(Result.Loading())
    val responseDepartment = _responseDepartment.asStateFlow()

    private val _responseSpecializationList = MutableStateFlow<PagingData<Specialization>>(PagingData.empty())
    val responseSpecializationList = _responseSpecializationList.asStateFlow()


    private val _responseGroup = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    val responseGroup = _responseGroup.asStateFlow()

    fun getDepartmentById(id:Int) {
        viewModelScope.launch {
            _responseDepartment.value = departmentRepository.getById(id = id)
        }
    }

    fun getSpecialization(departmentId: Int) {
        viewModelScope.launch {
            specializationRepository.getAll(
                departmentIds = listOf(departmentId)
            ).cachedIn(viewModelScope).collect {
                _responseSpecializationList.value = it
            }
        }
    }

    fun getGroups(departmentId: Int)  {
        viewModelScope.launch {
            groupRepository.getAll(departmentIds = listOf(departmentId))
                .cachedIn(viewModelScope).collect {
                    _responseGroup.value = it
                }
        }
    }
}