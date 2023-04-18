package ru.pgk63.core_common.api.speciality.model

data class CreateSpecializationBody(
    val number:String,
    val name:String,
    val nameAbbreviation:String,
    val qualification:String,
    val departmentId:Int
)