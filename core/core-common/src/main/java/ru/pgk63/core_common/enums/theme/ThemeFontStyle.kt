package ru.pgk63.core_common.enums.theme

import ru.pgk63.core_common.R
import androidx.annotation.StringRes
import androidx.compose.ui.text.font.FontFamily

enum class ThemeFontStyle(@StringRes val textId:Int, val fontFamily: FontFamily) {
    Default(R.string.default_value ,FontFamily.Default),
    Cursive(R.string.cursive, FontFamily.Cursive),
    Serif(R.string.serif, FontFamily.Serif),
    Monospace(R.string.monospace, FontFamily.Monospace),
    SansSerif(R.string.sans_serif, FontFamily.SansSerif)
}