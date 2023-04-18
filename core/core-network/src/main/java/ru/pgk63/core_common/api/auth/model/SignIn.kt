package ru.pgk63.core_common.api.auth.model

import com.google.gson.annotations.SerializedName
import ru.pgk63.core_common.api.language.model.Language
import ru.pgk63.core_common.enums.theme.ThemeCorners
import ru.pgk63.core_common.enums.theme.ThemeFontSize
import ru.pgk63.core_common.enums.theme.ThemeFontStyle
import ru.pgk63.core_common.enums.theme.ThemeStyle
import ru.pgk63.core_common.enums.user.UserRole

data class SignIn(
    val firstName:String,
    val lastName:String,
    val password:String
)

data class SignInResponse(
    val accessToken:String,
    val refreshToken:String,
    val userId:Int,
    val groupId:Int,
    val userRole: UserRole,
    @SerializedName("drarkMode")
    val darkMode: Boolean,
    val themeStyle: ThemeStyle = ThemeStyle.Green,
    val themeFontStyle: ThemeFontStyle = ThemeFontStyle.Default,
    val themeFontSize: ThemeFontSize = ThemeFontSize.Medium,
    val themeCorners: ThemeCorners = ThemeCorners.Rounded,
    val language: Language?,
    val emailVerification:Boolean,
    val telegramId:Int,
    val email:String,
    val errorMessage:String? = null
)