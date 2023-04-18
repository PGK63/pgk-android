package ru.pgk63.core_model.journal

import kotlinx.serialization.ExperimentalSerializationApi
import ru.pgk63.core_model.serialozation.DateSerialization
import java.util.Date

data class JournalTopicResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results:List<JournalTopic>
)
@kotlinx.serialization.Serializable
data class JournalTopic @OptIn(ExperimentalSerializationApi::class) constructor(
    val id:Int,
    val title:String,
    val homeWork:String?,
    val hours:Int,
    @kotlinx.serialization.Serializable(DateSerialization::class)
    val date:Date
)