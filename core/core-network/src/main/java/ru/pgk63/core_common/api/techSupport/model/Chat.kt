package ru.pgk63.core_common.api.techSupport.model

import ru.pgk63.core_common.api.user.model.User

data class ChatResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results:List<Chat>
)

data class Chat(
    val id: Int,
    val dateCreation: String,
    val lastMessage: Message,
    val messageCount: Int,
    val users: List<User>
)