package ru.pgk63.feature_main.screens.searchScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.department.repository.DepartmentRepository
import ru.pgk63.core_model.departmentHead.DepartmentHead
import ru.pgk63.core_common.api.departmentHead.repository.DepartmentHeadRepository
import ru.pgk63.core_common.api.director.model.Director
import ru.pgk63.core_common.api.director.repository.DirectorRepository
import ru.pgk63.core_common.api.group.repository.GroupRepository
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.speciality.repository.SpecializationRepository
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_common.api.student.repository.StudentRepository
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_common.api.subject.repository.SubjectRepository
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val departmentRepository: DepartmentRepository,
    private val departmentHeadRepository: DepartmentHeadRepository,
    private val directorRepository: DirectorRepository,
    private val groupRepository: GroupRepository,
    private val specializationRepository: SpecializationRepository,
    private val subjectRepository: SubjectRepository,
    private val teacherRepository: TeacherRepository
): ViewModel() {

    private val _responseStudentList = MutableStateFlow<PagingData<Student>>(PagingData.empty())
    val responseStudentList = _responseStudentList.asStateFlow()

    private val _responseDepartmentList = MutableStateFlow<PagingData<Department>>(PagingData.empty())
    val responseDepartmentList = _responseDepartmentList.asStateFlow()

    private val _responseDepartmentHeadList = MutableStateFlow<PagingData<DepartmentHead>>(PagingData.empty())
    val responseDepartmentHeadList = _responseDepartmentHeadList.asStateFlow()

    private val _responseDirectorList = MutableStateFlow<PagingData<Director>>(PagingData.empty())
    val responseDirectorList = _responseDirectorList.asStateFlow()

    private val _responseGroupList = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    val responseGroupList = _responseGroupList.asStateFlow()

    private val _responseSpecializationList = MutableStateFlow<PagingData<Specialization>>(PagingData.empty())
    val responseSpecializationList = _responseSpecializationList.asStateFlow()

    private val _responseSubjectList = MutableStateFlow<PagingData<Subject>>(PagingData.empty())
    val responseSubjectList = _responseSubjectList.asStateFlow()

    private val _responseTeacherList = MutableStateFlow<PagingData<Teacher>>(PagingData.empty())
    val responseTeacherList = _responseTeacherList.asStateFlow()

    fun getStudentList(search: String? = null) {
        viewModelScope.launch {
            studentRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseStudentList.value = it
            }
        }
    }

    fun getDepartmentList(search: String? = null) {
        viewModelScope.launch {
            departmentRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseDepartmentList.value = it
            }
        }
    }

    fun getDepartmentHeadList(search: String? = null) {
        viewModelScope.launch {
            departmentHeadRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseDepartmentHeadList.value = it
            }
        }
    }

    fun getDirectorList(search: String? = null) {
        viewModelScope.launch {
            directorRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseDirectorList.value = it
            }
        }
    }

    fun getGroupList(search: String? = null) {
        viewModelScope.launch {
            groupRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseGroupList.value = it
            }
        }
    }

    fun getSpecializationList(search: String? = null) {
        viewModelScope.launch {
            specializationRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseSpecializationList.value = it
            }
        }
    }

    fun getSubjectList(search: String? = null) {
        viewModelScope.launch {
            subjectRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseSubjectList.value = it
            }
        }
    }

    fun getTeacherList(search: String? = null) {
        viewModelScope.launch {
            teacherRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseTeacherList.value = it
            }
        }
    }
}