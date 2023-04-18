package ru.pgk63.core_model.deputyHeadma

import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.group.Group
@kotlinx.serialization.Serializable
data class DeputyHeadma(
    val id:Int,
    val firstName:String,
    val lastName:String,
    val middleName:String? = null,
    val photoUrl:String? = null,
    val group: Group,
    val department: Department
)