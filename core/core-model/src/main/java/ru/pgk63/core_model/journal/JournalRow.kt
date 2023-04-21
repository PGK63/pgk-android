package ru.pgk63.core_model.journal

import ru.pgk63.core_model.student.Student

data class JournalRowResponse(
    val results:List<JournalRow>
)
@kotlinx.serialization.Serializable
data class JournalRow(
    val id:Int,
    val student: Student,
    val columns: List<JournalColumn>
)