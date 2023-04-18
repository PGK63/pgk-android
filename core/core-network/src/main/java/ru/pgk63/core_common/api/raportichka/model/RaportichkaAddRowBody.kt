package ru.pgk63.core_common.api.raportichka.model

data class RaportichkaAddRowBody(
    val numberLesson:Int,
    val hours:Int,
    val subjectId:Int,
    val studentId:Int,
    val teacherId:Int
)