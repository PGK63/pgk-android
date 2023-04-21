package ru.pgk63.core_model.group

import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.teacher.model.Teacher

data class GroupResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results:List<Group>
)
@kotlinx.serialization.Serializable
data class Group(
    val id:Int,
    val course:Int,
    val number:Int,
    val speciality: Specialization,
    val classroomTeacher: Teacher,
    val headman: Student? = null,
    val deputyHeadma: Student? = null
){
    override fun toString(): String {
        return speciality.nameAbbreviation +
                "-${course}" + "$number"
    }
}