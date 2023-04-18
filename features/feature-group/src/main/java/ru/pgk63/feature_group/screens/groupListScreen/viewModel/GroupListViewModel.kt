package ru.pgk63.feature_group.screens.groupListScreen.viewModel

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
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.group.repository.GroupRepository
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class GroupListViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    userDataSource: UserDataSource
): ViewModel() {

    private val _responseGroup = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    val responseGroup = _responseGroup.asStateFlow()

    val responseUserLocal = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun getGroups(search:String? = null) {
        viewModelScope.launch {
            groupRepository.getAll(search = search).cachedIn(viewModelScope).collect {
                _responseGroup.value = it
            }
        }
    }
}