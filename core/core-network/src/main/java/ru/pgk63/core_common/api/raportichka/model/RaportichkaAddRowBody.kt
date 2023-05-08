package ru.pgk63.core_common.api.raportichka.model

import ru.pgk63.core_model.raportichka.RaportichkaCause

data class RaportichkaAddRowBody(
    val numberLesson:Int,
    val hours:Int,
    val subjectId:Int,
    val studentId:List<Int>,
    val teacherId:Int,
    val cause: RaportichkaCause
)