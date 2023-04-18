package ru.pgk63.feature_main.screens.notificationListScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.user.model.Notification
import ru.pgk63.core_common.api.user.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _responseNotificationList = MutableStateFlow<PagingData<Notification>>(PagingData.empty())
    val responseNotificationList = _responseNotificationList.asStateFlow()

    fun getNotifications(search: String?){
        viewModelScope.launch {
            userRepository.getNotifications(search = search).cachedIn(viewModelScope).collect {
                _responseNotificationList.value = it
            }
        }
    }
}