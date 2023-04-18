package ru.pgk63.core_common.api.user.model

import ru.pgk63.core_common.enums.user.UserRole

data class UserRegistrationBody(
    val firstName:String,
    val lastName:String,
    val middleName:String?
)

data class UserRegistrationResponse(
    val userId:Int,
    val passowrd:String,
    val userRole: UserRole
)