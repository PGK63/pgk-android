package ru.pgk63.core_common.enums.theme

import androidx.annotation.StringRes
import ru.pgk63.core_common.R

enum class ThemeFontSize(@StringRes val textId:Int) {
    Small(R.string.small),
    Medium(R.string.medium),
    Big(R.string.big)
}