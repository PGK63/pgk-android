package ru.pgk63.core_model.student

data class StudentRegistrationBody(
    val firstName:String,
    val lastName:String,
    val middleName:String?,
    val groupId:Int
)