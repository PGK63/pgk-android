package ru.pgk63.core_model.journal

data class CreateJournalBody(
    val course:Int,
    val semester:Int,
    val groupId:Int
)