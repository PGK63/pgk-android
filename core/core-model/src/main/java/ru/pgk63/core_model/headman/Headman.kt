package ru.pgk63.core_model.headman

import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.group.Group
@kotlinx.serialization.Serializable
data class Headman(
    val id:Int,
    val firstName:String,
    val lastName:String,
    val middleName:String? = null,
    val photoUrl:String? = null,
    val group: Group,
    val department: Department
)