package ru.pgk63.core_model.raportichka

import ru.pgk63.core_model.student.Student
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_common.api.teacher.model.Teacher

data class RaportichkaRow(
    val id:Int = 0,
    val numberLesson:Int,
    val confirmation:Boolean,
    val hours:Int = 2,
    val subject: Subject,
    val teacher:Teacher,
    val student: Student,
    val cause: RaportichkaCause = RaportichkaCause.PRIKAZ
)