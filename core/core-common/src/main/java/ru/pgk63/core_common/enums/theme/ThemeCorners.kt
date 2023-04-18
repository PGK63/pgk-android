package ru.pgk63.core_common.enums.theme

import ru.pgk63.core_common.R
import androidx.annotation.StringRes

enum class ThemeCorners(@StringRes val textId:Int) {
    Flat(R.string.flat),
    Rounded(R.string.rounded)
}