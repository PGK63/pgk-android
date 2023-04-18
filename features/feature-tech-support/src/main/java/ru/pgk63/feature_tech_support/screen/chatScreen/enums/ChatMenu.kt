package ru.pgk63.feature_tech_support.screen.chatScreen.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import ru.pgk63.core_ui.R

internal enum class ChatMenu(@StringRes val nameId: Int, val icon: ImageVector) {
    SEARCH_MESSAGES(R.string.search, Icons.Default.Search),
    PIN_MESSAGES(R.string.pin_messages, Icons.Default.PushPin),
    CLEAR_CHAT(R.string.clear_chat, Icons.Default.Delete)
}