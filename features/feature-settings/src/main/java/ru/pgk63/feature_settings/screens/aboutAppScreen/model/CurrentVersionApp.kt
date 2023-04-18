package ru.pgk63.feature_settings.screens.aboutAppScreen.model

import java.util.Date

data class CurrentVersionApp(
    val installDate: Date,
    val updateDate: Date,
    val versionCode: Int,
    val versionName: String
)