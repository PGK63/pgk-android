package ru.pgk63.feature_profile.screens.profileUpdateScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.departmentHead.repository.DepartmentHeadRepository
import ru.pgk63.core_common.api.director.repository.DirectorRepository
import ru.pgk63.core_common.api.journal.UpdateInformationBody
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import ru.pgk63.core_common.api.user.model.UpdateCabinetBody
import ru.pgk63.core_common.api.user.repository.UserRepository
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.addCharAtIndex
import javax.inject.Inject

@HiltViewModel
class ProfileUpdateViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userDataSource: UserDataSource,
    private val teacherRepository: TeacherRepository,
    private val departmentHeadRepository: DepartmentHeadRepository,
    private val directorRepository: DirectorRepository
): ViewModel() {

    private val _responseCabinet = MutableStateFlow<String?>(null)
    val responseCabinet = _responseCabinet.filterNotNull()

    private val _responseInformation = MutableStateFlow<String?>(null)
    val responseInformation = _responseInformation.filterNotNull()

    private val _responseUpdateResult = MutableStateFlow<Result<Unit?>?>(null)
    val responseUpdateResult = _responseUpdateResult.asStateFlow()

    fun informationChange(information: String) {
        viewModelScope.launch {
            _responseInformation.value = information
        }
    }

    fun cabinetChange(cabinet: String) {
        viewModelScope.launch {
            _responseCabinet.value = cabinet
        }
    }

    fun updateCabinet(cabinet: String) {
        viewModelScope.launch {
            val response = userRepository.updateCabinet(
                UpdateCabinetBody(cabinet = cabinet.addCharAtIndex('/',3))
            )

            _responseUpdateResult.value = response
        }
    }

    fun updateInformation(information: String) {
        viewModelScope.launch {
            val response = userRepository.updateInformation(
                UpdateInformationBody(information = information)
            )

            _responseUpdateResult.value = response
        }
    }

    fun getUser() {
        viewModelScope.launch {
            val user = userDataSource.get().first()

            user.userId?.let { userId ->
                when (user.userRole) {
                    UserRole.TEACHER -> getTeacherById(userId)
                    UserRole.DEPARTMENT_HEAD -> getDepartmentHeadById(userId)
                    UserRole.DIRECTOR -> getDirectorById(userId)
                    else -> Unit
                }
            }
        }
    }

    private fun getTeacherById(teacherId: Int) {
        viewModelScope.launch {
            val teacher = teacherRepository.getById(teacherId)

            _responseCabinet.value = teacher.data?.cabinet
            _responseInformation.value = teacher.data?.information
        }
    }

    private fun getDepartmentHeadById(departmentHead: Int) {
        viewModelScope.launch {
            val teacher = departmentHeadRepository.getById(departmentHead)

            _responseCabinet.value = teacher.data?.cabinet
            _responseInformation.value = teacher.data?.information
        }
    }

    private fun getDirectorById(directorId: Int) {
        viewModelScope.launch {
            val teacher = directorRepository.getById(directorId)

            _responseCabinet.value = teacher.data?.cabinet
            _responseInformation.value = teacher.data?.information
        }
    }
}