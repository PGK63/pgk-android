package com.example.feature_guide.screens.departmentHeadDetailsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_model.departmentHead.DepartmentHead
import ru.pgk63.core_common.api.departmentHead.repository.DepartmentHeadRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class DepartmentHeadDetailsViewModel @Inject constructor(
    private val departmentHeadRepository: DepartmentHeadRepository
): ViewModel() {

    private val _responseDepartmentHeadDetails = MutableStateFlow<Result<DepartmentHead>>(Result.Loading())
    val responseDepartmentHeadDetails = _responseDepartmentHeadDetails.asStateFlow()

    fun getDepartmentHeadDetails(departmentHeadId: Int) {
        viewModelScope.launch {
            _responseDepartmentHeadDetails.value = departmentHeadRepository.getById(departmentHeadId)
        }
    }
}