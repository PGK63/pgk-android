package ru.pgk63.feature_raportichka.screens.raportichkaAddRow.viewModel

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
import ru.pgk63.core_common.api.headman.repository.HeadmanRepository
import ru.pgk63.core_common.api.raportichka.model.RaportichkaAddRowBody
import ru.pgk63.core_common.api.raportichka.model.RaportichkaUpdateRowBody
import ru.pgk63.core_common.api.raportichka.repository.RaportichkaRepository
import ru.pgk63.core_common.api.student.repository.StudentRepository
import ru.pgk63.core_common.api.subject.repository.SubjectRepository
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class RaportichkaAddRowViewModel @Inject constructor(
    private val raportichkaRepository: RaportichkaRepository,
    private val subjectRepository: SubjectRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val headmanRepository: HeadmanRepository,
    userDataSource: UserDataSource
): ViewModel() {

    val user = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    private val _responseSubject = MutableStateFlow<PagingData<ru.pgk63.core_model.subject.Subject>>(PagingData.empty())
    val responseSubject = _responseSubject.asStateFlow()

    private val _responseStudent = MutableStateFlow<PagingData<ru.pgk63.core_model.student.Student>>(PagingData.empty())
    val responseStudent = _responseStudent.asStateFlow()

    private val _responseTeacher = MutableStateFlow<PagingData<Teacher>>(PagingData.empty())
    val responseTeacher = _responseTeacher.asStateFlow()

    private val _responseRaportichkaAddRow = MutableStateFlow<Result<Unit?>?>(null)
    val responseRaportichkaAddRow = _responseRaportichkaAddRow.asStateFlow()

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

    fun raportichkaAddRow(
        raportichkaId:Int,
        body: RaportichkaAddRowBody
    ){
        viewModelScope.launch {
            val response = raportichkaRepository.raportichkaAddRow(raportichkaId, body)
            _responseRaportichkaAddRow.value = response
        }
    }

    fun updateRow(
        rowId:Int,
        body: RaportichkaUpdateRowBody
    ){
        viewModelScope.launch {
            _responseRaportichkaAddRow.value = raportichkaRepository.updateRow(rowId, body)
        }
    }

    fun updateRow(
        rowId: Int,
        body: ru.pgk63.core_model.headman.HeadmanUpdateRaportichkaRowBody
    ){
        viewModelScope.launch {
            _responseRaportichkaAddRow.value = headmanRepository.updateRaportichkaRow(rowId, body)
        }
    }

    fun responseRaportichkaAddRowToNull() {
        viewModelScope.launch {
            _responseRaportichkaAddRow.value = null
        }
    }
}