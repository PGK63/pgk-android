package ru.pgk63.feature_specialization.screens.createSpecializationScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.department.repository.DepartmentRepository
import ru.pgk63.core_common.api.speciality.model.CreateSpecializationBody
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.speciality.repository.SpecializationRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class CreateSpecializationViewModel @Inject constructor(
    private val specializationRepository: SpecializationRepository,
    private val departmentRepository: DepartmentRepository
): ViewModel() {

    private val _responseCreateSpecializationResult = MutableStateFlow<Result<Specialization>?>(null)
    val responseCreateSpecializationResult = _responseCreateSpecializationResult.asStateFlow()

    private val _responseDepartmentList = MutableStateFlow<PagingData<ru.pgk63.core_model.department.Department>>(PagingData.empty())
    val responseDepartmentList = _responseDepartmentList.asStateFlow()

    fun createSpecialization(body: CreateSpecializationBody) {
        viewModelScope.launch {
            _responseCreateSpecializationResult.value = specializationRepository.create(body)
        }
    }

    fun getDepartmentList(search: String? = null) {
        viewModelScope.launch {
            departmentRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseDepartmentList.value = it
            }
        }
    }

    fun responseCreateSpecializationResultToNull() {
        _responseCreateSpecializationResult.value = null
    }
}