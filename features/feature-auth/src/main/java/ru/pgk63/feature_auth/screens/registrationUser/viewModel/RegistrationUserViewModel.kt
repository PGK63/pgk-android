package ru.pgk63.feature_auth.screens.registrationUser.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.admin.repository.AdminRepository
import ru.pgk63.core_common.api.departmentHead.repository.DepartmentHeadRepository
import ru.pgk63.core_common.api.director.repository.DirectorRepository
import ru.pgk63.core_common.api.educationalSector.repository.EducationalSectorRepository
import ru.pgk63.core_common.api.headman.repository.HeadmanRepository
import ru.pgk63.core_common.api.student.repository.StudentRepository
import ru.pgk63.core_common.api.teacher.repository.TeacherRepository
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.api.user.model.UserRegistrationResponse
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.feature_auth.screens.registrationUser.model.RegistrationUserState
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class RegistrationUserViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val departmentHeadRepository: DepartmentHeadRepository,
    private val directorRepository: DirectorRepository,
    private val educationalSectorRepository: EducationalSectorRepository,
    private val headmanRepository: HeadmanRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository
): ViewModel() {

    private val _responseStudents = MutableStateFlow<PagingData<ru.pgk63.core_model.student.Student>>(PagingData.empty())
    val responseStudents = _responseStudents.asStateFlow()

    private val _responseResultRegistration = MutableStateFlow<Result<UserRegistrationResponse>?>(null)
    val responseResultRegistration = _responseResultRegistration.asStateFlow()

    fun getStudents(search: String? = null, groupId:Int? = null) {
        viewModelScope.launch {
            studentRepository.getAll(
                search = search,
                groupIds = if(groupId == null) listOf() else listOf(groupId)
            ).cachedIn(viewModelScope).collect {
                _responseStudents.value = it
            }
        }
    }

    fun registration(type: RegistrationUserState) {

        _responseResultRegistration.value = Result.Loading()

        when(type) {
            is RegistrationUserState.Base -> baseRegistration(type.body, type.userRole)
            is RegistrationUserState.Headman -> if(type.deputy)
                registrationHeadmanDeputy(type.body)
            else
                registrationHeadman(type.body)
            is RegistrationUserState.Student -> registrationStudent(type.body)
        }
    }

    private fun baseRegistration(body: UserRegistrationBody, userRole: UserRole) {
        when(userRole){
            UserRole.TEACHER -> registrationTeacher(body)
            UserRole.EDUCATIONAL_SECTOR -> registrationEducationalSector(body)
            UserRole.DEPARTMENT_HEAD -> registrationDepartmentHead(body)
            UserRole.DIRECTOR -> registrationDirector(body)
            UserRole.ADMIN -> registrationAdmin(body)
            else -> Unit
        }
    }

    private fun registrationAdmin(body: UserRegistrationBody) {
        viewModelScope.launch {
            _responseResultRegistration.value = adminRepository.registration(body)
        }
    }

    private fun registrationDepartmentHead(body: UserRegistrationBody) {
        viewModelScope.launch {
            _responseResultRegistration.value = departmentHeadRepository.registration(body)
        }
    }

    private fun registrationDirector(body: UserRegistrationBody) {
        viewModelScope.launch {
            _responseResultRegistration.value = directorRepository.registration(body)
        }
    }

    private fun registrationEducationalSector(body: UserRegistrationBody) {
        viewModelScope.launch {
            _responseResultRegistration.value = educationalSectorRepository.registration(body)
        }
    }

    private fun registrationTeacher(body: UserRegistrationBody) {
        viewModelScope.launch {
            _responseResultRegistration.value = teacherRepository.registration(body)
        }
    }

    private fun registrationHeadman(body: ru.pgk63.core_model.headman.HeadmanRegistrationBody) {
        viewModelScope.launch {
            _responseResultRegistration.value = headmanRepository.registration(body)
        }
    }

    private fun registrationHeadmanDeputy(body: ru.pgk63.core_model.headman.HeadmanRegistrationBody) {
        viewModelScope.launch {
            _responseResultRegistration.value = headmanRepository.registrationDeputy(body)
        }
    }

    private fun registrationStudent(body: ru.pgk63.core_model.student.StudentRegistrationBody) {
        viewModelScope.launch {
            _responseResultRegistration.value = studentRepository.registration(body)
        }
    }
}