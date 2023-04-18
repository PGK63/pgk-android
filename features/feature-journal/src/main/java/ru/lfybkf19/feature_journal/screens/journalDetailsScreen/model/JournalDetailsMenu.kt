package ru.lfybkf19.feature_journal.screens.journalDetailsScreen.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import ru.pgk63.core_ui.R

enum class JournalDetailsMenu(@StringRes val nameId: Int, val icon: ImageVector) {
    CONTENT_SUBJECT(R.string.content, Icons.Default.Sort),
    DOWNLOAD(R.string.download_journal, Icons.Default.Download),
    CREATE_JOURNAL_SUBJECT(R.string.journal_subject_create, Icons.Default.Add),
    NETWORK_MODE(R.string.network, Icons.Default.NetworkWifi)
}