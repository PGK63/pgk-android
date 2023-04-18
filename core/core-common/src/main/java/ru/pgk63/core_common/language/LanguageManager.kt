package ru.pgk63.core_common.language

import android.content.Context
import java.util.*

fun Context.setLocale(code: String): Context? {
    val locale = Locale(code)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return createConfigurationContext(config)
}