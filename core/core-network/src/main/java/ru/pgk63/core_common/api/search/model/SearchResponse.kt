package ru.pgk63.core_common.api.search.model

import ru.pgk63.core_model.group.Group
import ru.pgk63.core_model.departmentHead.DepartmentHead
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.user.model.User

data class SearchResponse(
    val students:List<ru.pgk63.core_model.student.Student>,
    val headmens:List<ru.pgk63.core_model.headman.Headman>,
    val departmentHead:List<DepartmentHead>,
    val teachers:List<Teacher>,
    val educationalSectors:List<User>,
    val deputyHeadman:List<ru.pgk63.core_model.deputyHeadma.DeputyHeadma>,
    val admins:List<User>,
    val departments:List<ru.pgk63.core_model.department.Department>,
    val groups:List<Group>,
    val specialities:List<Specialization>,
    val subjects:List<ru.pgk63.core_model.subject.Subject>
)