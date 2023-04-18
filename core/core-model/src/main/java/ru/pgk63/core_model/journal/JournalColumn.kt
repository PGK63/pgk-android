package ru.pgk63.core_model.journal

import kotlinx.serialization.ExperimentalSerializationApi
import ru.pgk63.core_model.serialozation.DateSerialization
import java.util.Date

data class JournalColumnResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results:List<JournalColumn>
)
@kotlinx.serialization.Serializable
data class JournalColumn @OptIn(ExperimentalSerializationApi::class) constructor(
    val id:Int,
    val evaluation: JournalEvaluation,
    @kotlinx.serialization.Serializable(DateSerialization::class)
    val date:Date,
    val row: JournalRow
)