package ru.pgk63.core_database.room.database.journal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal")
data class JournalEntity(
    @PrimaryKey(autoGenerate = false) val id:Int,
    val semester: Int,
    val course: Int,
    @ColumnInfo(name = "group", defaultValue = "null") val group: String?,
    @ColumnInfo(name = "groupId", defaultValue = "null") val groupId: Int?,
    val journalSubjectList: String,
    val studentList: String
)

data class JournalEntityListItem(
    val id: Int,
    val semester: Int,
    val course: Int,
    val group: String?,
    val groupId: Int?
)