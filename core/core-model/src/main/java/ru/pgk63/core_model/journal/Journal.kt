package ru.pgk63.core_model.journal

import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.deputyHeadma.DeputyHeadma
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_model.headman.Headman
import ru.pgk63.core_common.api.teacher.model.Teacher

data class JournalResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results: List<Journal>
)
@kotlinx.serialization.Serializable
data class Journal(
    val id:Int,
    val course:Int,
    val semester:Int,
    val group: Group,
    val department: Department,
    val classroomTeacher: Teacher,
    val headman: Headman? = null,
    val deputyHeadma: DeputyHeadma? = null
)