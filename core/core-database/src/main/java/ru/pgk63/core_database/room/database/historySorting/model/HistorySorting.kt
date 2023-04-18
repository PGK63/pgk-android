package ru.pgk63.core_database.room.database.historySorting.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user_history_sorting")
data class HistorySorting(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val contentId: Int,
    val name: String,
    val description: String?,
    val type: HistorySortingType,
    val date: Date = Date()
)

enum class HistorySortingType {
    RAPORTICHKA,
    JOURNAL
}
