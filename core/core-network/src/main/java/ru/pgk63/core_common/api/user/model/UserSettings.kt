package ru.pgk63.core_common.api.user.model

import com.google.gson.annotations.SerializedName
import ru.pgk63.core_common.api.language.model.Language
import ru.pgk63.core_common.enums.theme.ThemeCorners
import ru.pgk63.core_common.enums.theme.ThemeFontSize
import ru.pgk63.core_common.enums.theme.ThemeFontStyle
import ru.pgk63.core_common.enums.theme.ThemeStyle

data class UserSettings(
    @SerializedName("drarkMode")
    val darkMode: Boolean,
    val themeStyle: ThemeStyle = ThemeStyle.Green,
    val themeFontStyle: ThemeFontStyle = ThemeFontStyle.Default,
    val themeFontSize: ThemeFontSize = ThemeFontSize.Medium,
    val themeCorners: ThemeCorners = ThemeCorners.Rounded,
    val language: Language?,
    val includedNotifications: Boolean,
    val soundNotifications: Boolean,
    val vibrationNotifications: Boolean,
    val includedSchedulesNotifications: Boolean,
    val includedJournalNotifications: Boolean,
    val includedRaportichkaNotifications: Boolean,
    val includedTechnicalSupportNotifications: Boolean
)