package ru.pgk63.core_common.api.techSupport.model

import androidx.annotation.StringRes
import ru.pgk63.core_common.R

enum class MessageContentType(@StringRes val nameId: Int) {
    IMAGE(R.string.director),
    VIDEO(R.string.video),
    FILE(R.string.file)
}