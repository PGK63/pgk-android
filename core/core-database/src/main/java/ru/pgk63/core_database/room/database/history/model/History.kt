package ru.pgk63.core_database.room.database.history.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * @param historyId id model
 * @param contentId journal or raportichka...*/
@Entity(tableName = "user_history")
data class History(
    @PrimaryKey(autoGenerate = true) val historyId: Int = 0,
    val contentId: Int,
    val title: String,
    val description : String? = null,
    val historyType: HistoryType,
    val date: Date = Date()
)

enum class HistoryType {
    GROUP,
    DEPARTMENT,
    STUDENT,
    TEACHER,
    SUBJECT,
    SPECIALITY,
    DIRECTOR,
    DEPARTMENT_HEAD
}
