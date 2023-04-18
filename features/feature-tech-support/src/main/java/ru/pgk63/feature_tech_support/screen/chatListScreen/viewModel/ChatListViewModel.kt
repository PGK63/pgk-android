package ru.pgk63.feature_tech_support.screen.chatListScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.techSupport.paging.ChatPagingSource
import ru.pgk63.core_common.api.techSupport.repository.TechSupportRepository
import javax.inject.Inject

@HiltViewModel
internal class ChatListViewModel @Inject constructor(
    private val techSupportRepository: TechSupportRepository
): ViewModel() {

    val chats = Pager(PagingConfig(pageSize = PAGE_SIZE)){
        ChatPagingSource(techSupportRepository = techSupportRepository)
    }.flow.cachedIn(viewModelScope)
}