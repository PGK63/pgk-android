package ru.pgk63.feature_subject.screens.subjectDetailsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.subject.repository.SubjectRepository
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
class SubjectDetailsViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val teacherRepository: TeacherRepository
): ViewModel() {

    private val _responseSubject = MutableStateFlow<Result<ru.pgk63.core_model.subject.Subject>>(Result.Loading())
    val responseSubject = _responseSubject.asStateFlow()

    private val _responseTeachers = MutableStateFlow<PagingData<Teacher>>(PagingData.empty())
    val responseTeachers = _responseTeachers.asStateFlow()

    fun getSubjectById(id:Int){
        viewModelScope.launch {
            _responseSubject.value = subjectRepository.getById(id)
        }
    }

    fun getTeachers(search:String? = null, subjectId: Int){
        viewModelScope.launch {
            teacherRepository.getAll(
                search = search,
                subjectIds = listOf(subjectId)
            ).cachedIn(viewModelScope).collect {
                _responseTeachers.value = it
            }
        }
    }
}