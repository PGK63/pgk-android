package ru.pgk63.core_model.journal

import java.util.Date

data class CreateJournalTopicBody(
    val title:String,
    val homeWork:String?,
    val hours:Int,
    val date: Date = Date()
)