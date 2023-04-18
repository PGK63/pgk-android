package ru.pgk63.core_common.api.group.model

data class CreateGroupBody(
    val course:Int,
    val number:Int,
    val specialityId:Int,
    val departmentId:Int,
    val classroomTeacherId:Int
)

data class CreateGroupResponse(
    val id: Int
)