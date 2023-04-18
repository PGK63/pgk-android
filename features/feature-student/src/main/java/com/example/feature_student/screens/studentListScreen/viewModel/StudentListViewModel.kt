package com.example.feature_student.screens.studentListScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.student.repository.StudentRepository
import javax.inject.Inject

@HiltViewModel
internal class StudentListViewModel @Inject constructor(
    private val studentRepository: StudentRepository
): ViewModel() {

    private val _responseStudent = MutableStateFlow<PagingData<ru.pgk63.core_model.student.Student>>(PagingData.empty())
    val responseStudent = _responseStudent.asStateFlow()

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
}