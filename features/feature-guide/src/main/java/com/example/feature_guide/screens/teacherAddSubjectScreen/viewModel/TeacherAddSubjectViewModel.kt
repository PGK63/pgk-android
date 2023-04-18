package com.example.feature_guide.screens.teacherAddSubjectScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.subject.repository.SubjectRepository
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_model.subject.Subject
import javax.inject.Inject

@HiltViewModel
internal class TeacherAddSubjectViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository,
    private val subjectRepository: SubjectRepository
): ViewModel() {

    private val _responseTeacherAddSubjectResult = MutableStateFlow<Result<Unit?>?>(null)
    val responseTeacherAddSubjectResult = _responseTeacherAddSubjectResult.asStateFlow()

    private val _responseSubjectList = MutableStateFlow<PagingData<Subject>>(PagingData.empty())
    val responseSubjectList = _responseSubjectList.asStateFlow()

    fun getSubjectList(search: String? = null) {
        viewModelScope.launch {
            subjectRepository.getAll(
                search = search
            ).cachedIn(viewModelScope).collect {
                _responseSubjectList.value = it
            }
        }
    }

    fun teacherAddSubject(teacherId: Int, subjectId: Int) {
        viewModelScope.launch {
            _responseTeacherAddSubjectResult.value =
                teacherRepository.teacherAddSubject(teacherId, subjectId)
        }
    }
}