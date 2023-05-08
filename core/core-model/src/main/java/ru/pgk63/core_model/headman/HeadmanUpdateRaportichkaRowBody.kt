package ru.pgk63.core_model.headman

import ru.pgk63.core_model.raportichka.RaportichkaCause

data class HeadmanUpdateRaportichkaRowBody(
    val numberLesson:Int,
    val hours:Int,
    val subjectId:Int,
    val teacherId:Int,
    val studentId:Int,
    val raportichkaId:Int,
    val cause: RaportichkaCause
)