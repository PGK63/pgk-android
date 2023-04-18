package com.example.feature_student.screens.studentDetailsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.student.repository.StudentRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class StudentDetailsViewModel @Inject constructor(
    private val studentRepository: StudentRepository
): ViewModel() {

    private val _responseStudents = MutableStateFlow<Result<ru.pgk63.core_model.student.Student>>(Result.Loading())
    val responseStudents = _responseStudents.asStateFlow()

    fun getStudentById(id:Int){
        viewModelScope.launch {
            _responseStudents.value = studentRepository.getById(id)
        }
    }
}