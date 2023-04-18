package ru.pgk63.core_common.api.user.model

@kotlinx.serialization.Serializable
data class User(
    val id:Int = 0,
    val firstName:String = "",
    val lastName:String = "",
    val middleName:String? = null,
    val photoUrl:String? = null
)

data class UserDetails(
    val id:Int = 0,
    val firstName:String = "",
    val lastName:String = "",
    val middleName:String? = null,
    val photoUrl:String? = null,
    val telegramId:Int? = null,
    val email:String? = null,
    val emailVerification:Boolean
)