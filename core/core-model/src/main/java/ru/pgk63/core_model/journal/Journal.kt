package ru.pgk63.core_model.journal

import ru.pgk63.core_model.group.Group

data class JournalResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results: List<Journal>
)
@kotlinx.serialization.Serializable
data class Journal(
    val id:Int,
    val course:Int,
    val semester:Int,
    val group: Group
)