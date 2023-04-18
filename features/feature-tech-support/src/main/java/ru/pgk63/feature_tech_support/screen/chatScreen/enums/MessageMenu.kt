package ru.pgk63.feature_tech_support.screen.chatScreen.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import ru.pgk63.core_ui.R

internal enum class MessageMenu(@StringRes val nameId: Int, val icon: ImageVector) {
    COPY(R.string.copy, Icons.Default.CopyAll),
    PIN(R.string.pin, Icons.Default.PushPin),
    EDIT(R.string.edit, Icons.Default.Edit),
    DELETE(R.string.delete, Icons.Default.Delete)
}