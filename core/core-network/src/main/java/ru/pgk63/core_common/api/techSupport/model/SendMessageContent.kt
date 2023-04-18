package ru.pgk63.core_common.api.techSupport.model

import android.net.Uri

data class SendMessageContent(
    val type: MessageContentType,
    val uri: Uri
)