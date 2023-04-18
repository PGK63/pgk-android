package ru.pgk63.feature_department.screens.departmentListScreen.viewModel

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
import ru.pgk63.core_common.api.department.repository.DepartmentRepository
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class DepartmentListViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository,
    userDataSource: UserDataSource
): ViewModel() {

    private val _responseDepartmentList = MutableStateFlow<PagingData<ru.pgk63.core_model.department.Department>>(PagingData.empty())
    val responseDepartmentList = _responseDepartmentList.asStateFlow()

    val responseUserLocal = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun getDepartments(search:String? = null) {
        viewModelScope.launch {
            departmentRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseDepartmentList.value = it
            }
        }
    }
}