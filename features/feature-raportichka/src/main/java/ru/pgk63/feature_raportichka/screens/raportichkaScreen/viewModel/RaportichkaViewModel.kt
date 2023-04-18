package ru.pgk63.feature_raportichka.screens.raportichkaScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.group.repository.GroupRepository
import ru.pgk63.core_common.api.headman.repository.HeadmanRepository
import ru.pgk63.core_common.api.raportichka.model.Raportichka
import ru.pgk63.core_common.api.raportichka.repository.RaportichkaRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class RaportichkaViewModel @Inject constructor(
    private val raportichkaRepository: RaportichkaRepository,
    private val groupRepository: GroupRepository,
    private val headmanRepository: HeadmanRepository,
    userDataSource: UserDataSource
): ViewModel() {

    val user = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    private val _responseAddOrDeleteRow = MutableStateFlow<Result<Unit?>?>(null)
    val responseAddOrDeleteRow = _responseAddOrDeleteRow.asStateFlow()

    private val _responseGroup = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    val responseGroup = _responseGroup.asStateFlow()

    private val _responseRaportichkaList = MutableStateFlow<PagingData<Raportichka>>(PagingData.empty())
    val responseRaportichkaList = _responseRaportichkaList.asStateFlow()

    fun getRaportichka(
        confirmation:Boolean? = null ,
        onlyDate:String? = null,
        startDate:String? = null,
        endDate:String? = null,
        groupIds:List<Int>? = null,
        subjectIds:List<Int>? = null,
        classroomTeacherIds:List<Int>? = null,
        numberLessons:List<Int>? = null,
        teacherIds:List<Int>? = null,
        studentIds:List<Int>? = null
    ) {
        viewModelScope.launch {
            raportichkaRepository.getRaportichkaAll(
                confirmation = confirmation,
                onlyDate = onlyDate,
                startDate = startDate,
                endDate = endDate,
                groupIds = groupIds,
                subjectIds = subjectIds,
                classroomTeacherIds = classroomTeacherIds,
                numberLessons = numberLessons,
                teacherIds = teacherIds,
                studentIds = studentIds
            ).cachedIn(viewModelScope).collect {
                _responseRaportichkaList.value = it
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

    fun deleteRow(rowId:Int) {
        viewModelScope.launch {
            _responseAddOrDeleteRow.value = raportichkaRepository.deleteRow(rowId)
        }
    }

    fun responseDeleteRowToNull() {
        viewModelScope.launch {
            _responseAddOrDeleteRow.value = null
        }
    }

    fun createRaportichka(groupId:Int) {
        viewModelScope.launch {
            _responseAddOrDeleteRow.value = groupRepository.createRaportichka(groupId)
        }
    }

    fun createRaportichka() {
        viewModelScope.launch {
            _responseAddOrDeleteRow.value = headmanRepository.createRaportichka()
        }
    }

    fun updateConfirmation(rowId: Int){
        viewModelScope.launch {
            _responseAddOrDeleteRow.value = raportichkaRepository.updateConfirmation(rowId)
        }
    }

}