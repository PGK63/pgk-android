package ru.pgk63.core_model.group

data class CreateGroupBody(
    val course:Int,
    val number:String,
    val specialityId:Int,
    val departmentId:Int,
    val classroomTeacherId:Int
)

data class CreateGroupResponse(
    val id: Int
)