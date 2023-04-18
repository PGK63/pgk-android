package ru.pgk63.core_database.user.model

import ru.pgk63.core_common.enums.theme.ThemeCorners
import ru.pgk63.core_common.enums.theme.ThemeFontSize
import ru.pgk63.core_common.enums.theme.ThemeFontStyle
import ru.pgk63.core_common.enums.theme.ThemeStyle
import ru.pgk63.core_common.enums.user.UserRole

@kotlinx.serialization.Serializable
data class UserLocalDatabase(
    val statusRegistration: Boolean = false,
    val userId: Int? = null,
    val groupId: Int? = null,
    val userRole: UserRole? = null,
    var darkMode: Boolean? = null,
    var themeStyle: ThemeStyle = ThemeStyle.Green,
    var themeFontStyle: ThemeFontStyle = ThemeFontStyle.Default,
    var themeFontSize: ThemeFontSize = ThemeFontSize.Medium,
    var themeCorners: ThemeCorners = ThemeCorners.Rounded,
    var languageCode: String? = null,
    val emailVerification:Boolean? = null,
    val telegramId:Int? = null,
    val email:String? = null,
    val firstName:String = "",
    val lastName:String = "",
    val password:String = "",
    val accessToken:String? = null,
    val refreshToken:String? = null,
    val downloadJournal: List<DownloadJournal> = emptyList()
)

@kotlinx.serialization.Serializable
data class DownloadJournal(
    val journalId: Int
)