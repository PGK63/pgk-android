package ru.lfybkf19.feature_journal.screens.journalDetailsScreen.model

import java.util.*

internal sealed class JournalDetailsBottomDrawerType {
    object JournalSubjectDetails: JournalDetailsBottomDrawerType()
    data class JournalColumn(
        val columnId: Int?,
        val rowId: Int?,
        val student: ru.pgk63.core_model.student.Student,
        val date: Date?,
        val evaluation: ru.pgk63.core_model.journal.JournalEvaluation?
    ): JournalDetailsBottomDrawerType()
}
