package com.example.feature_guide.screens.guideListScreen.viewModel

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
import ru.pgk63.core_model.departmentHead.DepartmentHead
import ru.pgk63.core_common.api.departmentHead.repository.DepartmentHeadRepository
import ru.pgk63.core_common.api.director.model.Director
import ru.pgk63.core_common.api.director.repository.DirectorRepository
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class GuideListViewModel @Inject constructor(
    private val departmentHeadRepository: DepartmentHeadRepository,
    private val directorRepository: DirectorRepository,
    private val teacherRepository: TeacherRepository,
    private val userDataSource: UserDataSource
): ViewModel() {

    private val _responseDepartmentHeadList = MutableStateFlow<PagingData<DepartmentHead>>(PagingData.empty())
    val responseDepartmentHeadList = _responseDepartmentHeadList.asStateFlow()

    private val _responseDirectorList = MutableStateFlow<PagingData<Director>>(PagingData.empty())
    val responseDirectorList = _responseDirectorList.asStateFlow()

    private val _responseTeacherList = MutableStateFlow<PagingData<Teacher>>(PagingData.empty())
    val responseTeacherList = _responseTeacherList.asStateFlow()

    val responseUserLocal = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun getDepartmentHeadList(search: String? = null) {
        viewModelScope.launch {
            departmentHeadRepository.getAll(
                search = search
            ).cachedIn(viewModelScope).collect {
                _responseDepartmentHeadList.value = it
            }
        }
    }

    fun getDirectorsList(search: String? = null) {
        viewModelScope.launch {
            directorRepository.getAll(
                search = search,
                current = true
            ).cachedIn(viewModelScope).collect {
                _responseDirectorList.value = it
            }
        }
    }

    fun getTeacherList(search: String? = null) {
        viewModelScope.launch {
            teacherRepository.getAll(
                search = search
            ).cachedIn(viewModelScope).collect {
                _responseTeacherList.value = it
            }
        }
    }
}