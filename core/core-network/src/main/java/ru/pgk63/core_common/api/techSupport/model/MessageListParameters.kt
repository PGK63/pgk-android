package ru.pgk63.core_common.api.techSupport.model

@kotlinx.serialization.Serializable
data class MessageListParameters(
    var search:String? = null,
    var pin: Boolean? = null,
    val userVisible:String? = null,
    val onlyDate:String? = null,
    val startDate:String? = null,
    val endDate:String? = null,
    var userId:Int? = null,
    val chatId:Int? = null
)