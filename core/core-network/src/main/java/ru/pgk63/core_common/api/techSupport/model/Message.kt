package ru.pgk63.core_common.api.techSupport.model

import ru.pgk63.core_common.kotlinxSerialization.DateTimeSerialization
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    @SerialName("Messages")
    val messages:List<Message>
)

@Serializable
data class Message(
    @SerialName("Id")
    val id:Int,
    @SerialName("Text")
    val text:String? = null,
    @SerialName("UserVisible")
    val userVisible:Boolean,
    @SerialName("Pin")
    val pin:Boolean,
    @SerialName("Edited")
    val edited:Boolean,
    @[SerialName("EditedDate") Serializable(with = DateTimeSerialization::class)]
    val editedDate:String? = null,
    @[SerialName("Date") Serializable(with = DateTimeSerialization::class)]
    val date:String,
    @SerialName("User")
    val user: MessageUser,
    @SerialName("Contents")
    val contents: List<MessageContent>
)

@Serializable
data class MessageUser(
    @SerialName("Id")
    val id:Int = 0,
    @SerialName("FirstName")
    val firstName:String = "",
    @SerialName("LastName")
    val lastName:String = "",
    @SerialName("MiddleName")
    val middleName:String? = null,
    @SerialName("PhotoUrl")
    val photoUrl:String? = null
)

@Serializable
data class MessageContent(
    @SerialName("Id")
    val id:Int,
    @SerialName("Url")
    val url:String,
    @SerialName("Type")
    val _type: Int
){
    val type: MessageContentType
        get() = MessageContentType.values()[_type]
}