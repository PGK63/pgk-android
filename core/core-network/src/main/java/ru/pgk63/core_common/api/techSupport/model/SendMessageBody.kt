package ru.pgk63.core_common.api.techSupport.model

data class SendMessageBody(
    val text:String,
    val chatId: Int? = null
)