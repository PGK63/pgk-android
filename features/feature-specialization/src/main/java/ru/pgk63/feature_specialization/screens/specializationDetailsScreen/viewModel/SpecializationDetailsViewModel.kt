package ru.pgk63.feature_specialization.screens.specializationDetailsScreen.viewModel

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
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.speciality.repository.SpecializationRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
class SpecializationDetailsViewModel @Inject constructor(
    private val specializationRepository: SpecializationRepository,
    private val groupRepository: GroupRepository
): ViewModel() {

    private val _responseSpecialization = MutableStateFlow<Result<Specialization>>(Result.Loading())
    val responseSpecialization = _responseSpecialization.asStateFlow()

    private val _responseGroup = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    val responseGroup = _responseGroup.asStateFlow()

    fun getById(id:Int){
        viewModelScope.launch {
            _responseSpecialization.value = specializationRepository.getById(id)
        }
    }

    fun getGroups(specialityId: Int) {
        viewModelScope.launch {
            groupRepository.getAll(specialityIds = listOf(specialityId)).cachedIn(viewModelScope)
                .collect {
                    _responseGroup.value = it
                }
        }
    }
}