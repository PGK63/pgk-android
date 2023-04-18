package ru.lfybkf19.feature_journal.screens.journalTopicTableScreen.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.vector.ImageVector
import ru.pgk63.core_ui.R

internal enum class JournalTopicRowMenu(
    @StringRes val textId: Int,
    val icon: ImageVector
) {
    DELETE(R.string.delete, Icons.Default.Delete)
}