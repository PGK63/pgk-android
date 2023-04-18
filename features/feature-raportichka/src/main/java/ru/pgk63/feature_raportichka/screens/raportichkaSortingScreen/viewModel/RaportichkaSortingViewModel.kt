package ru.pgk63.feature_raportichka.screens.raportichkaSortingScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.group.repository.GroupRepository
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_common.api.student.repository.StudentRepository
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_common.api.subject.repository.SubjectRepository
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import javax.inject.Inject

@HiltViewModel
class RaportichkaSortingViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val groupRepository: GroupRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository
): ViewModel() {

    private val _responseSubject = MutableStateFlow<PagingData<Subject>>(PagingData.empty())
    val responseSubject = _responseSubject.asStateFlow()

    private val _responseGroup = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    val responseGroup = _responseGroup.asStateFlow()

    private val _responseStudent = MutableStateFlow<PagingData<Student>>(PagingData.empty())
    val responseStudent = _responseStudent.asStateFlow()

    private val _responseTeacher = MutableStateFlow<PagingData<Teacher>>(PagingData.empty())
    val responseTeacher = _responseTeacher.asStateFlow()

    fun getSubjects(search:String? = null, teacherIds:List<Int>? = null) {
        viewModelScope.launch {
            subjectRepository.getAll(
                search = search,
                teacherIds = teacherIds
            ).cachedIn(viewModelScope).collect {
                _responseSubject.value = it
            }
        }
    }

    fun getGroups(search:String? = null) {
        viewModelScope.launch {
            groupRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseGroup.value = it
            }
        }
    }

    fun getStudents(
        search: String? = null,
        groupIds: List<Int>? = null,
    ) {
        viewModelScope.launch {
            studentRepository.getAll(
                search = search,
                groupIds = groupIds
            ).cachedIn(viewModelScope).collect {
                _responseStudent.value = it
            }
        }
    }

    fun getTeacher(
        search:String? = null,
        subjectIds:List<Int>? = null
    ){
        viewModelScope.launch {
            teacherRepository.getAll(
                search = search,
                subjectIds = subjectIds
            ).cachedIn(viewModelScope).collect {
                _responseTeacher.value = it
            }
        }
    }
}