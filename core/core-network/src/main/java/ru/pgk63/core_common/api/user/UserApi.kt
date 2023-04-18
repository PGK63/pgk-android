package ru.pgk63.core_common.api.user

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.journal.UpdateInformationBody
import ru.pgk63.core_common.api.user.model.*
import ru.pgk63.core_common.common.Constants.CUSTOM_HEADER
import ru.pgk63.core_common.common.Constants.NO_AUTH
import ru.pgk63.core_common.enums.theme.ThemeCorners
import ru.pgk63.core_common.enums.theme.ThemeFontSize
import ru.pgk63.core_common.enums.theme.ThemeFontStyle
import ru.pgk63.core_common.enums.theme.ThemeStyle

interface UserApi {

    @GET("/pgk63/api/User")
    suspend fun get(): Response<UserDetails>

    @PATCH("/pgk63/api/User/Information")
    suspend fun updateInformation(@Body body: UpdateInformationBody): Response<Unit?>

    @PATCH("/pgk63/api/User/Cabinet")
    suspend fun updateCabinet(@Body body: UpdateCabinetBody): Response<Unit?>

    @GET("/pgk63/api/User/Notifications")
    suspend fun getNotifications(
        @Query("search") search:String? = null,
        @Query("pageNumber") pageNumber:Int,
        @Query("pageSize") pageSize:Int = PAGE_SIZE
    ): NotificationResponse

    @PATCH("/pgk63/api/User/Settings/Notifications")
    suspend fun updateNotificationSettings(
        @Body body: NotificationSetting
    ): Response<Unit?>

    @PATCH("/pgk63/api/User/Password")
    suspend fun updatePassword(): Response<String>

    @PATCH("/pgk63/api/User/Settings/DrarkMode")
    suspend fun updateDarkMode(): Response<UserSettings>

    @PATCH("/pgk63/api/User/Settings/ThemeStyle")
    suspend fun updateThemeStyle(@Query("themeStyle") themeStyle: ThemeStyle): Response<UserSettings>

    @PATCH("/pgk63/api/User/Settings/ThemeFontStyle")
    suspend fun updateThemeFontStyle(
        @Query("themeFontStyle") themeFontStyle: ThemeFontStyle
    ): Response<UserSettings>

    @PATCH("/pgk63/api/User/Settings/ThemeFontSize")
    suspend fun updateThemeFontSize(
        @Query("themeFontSize") themeFontSize: ThemeFontSize
    ): Response<UserSettings>

    @PATCH("/pgk63/api/User/Settings/ThemeCorners")
    suspend fun updateThemeCorners(
        @Query("themeCorners") themeCorners: ThemeCorners
    ): Response<UserSettings>

    @GET("/pgk63/api/User/Settings")
    suspend fun getSettings(): Response<UserSettings>

    @POST("/pgk63/api/User/Photo")
    @Multipart
    suspend fun uploadImage(
        @Part photo: MultipartBody.Part
    ): Response<UpdateUserPhotoResponse>

    @Headers("$CUSTOM_HEADER: $NO_AUTH")
    @POST("/pgk63/api/User/Email/Pasword/Reset")
    suspend fun passwordReset(
        @Query("email") email: String
    ): Response<Unit?>

    @PATCH("/pgk63/api/User/Email")
    suspend fun updateEmail(
        @Query("newEmail") email: String
    ): Response<Unit?>

    @POST("/pgk63/api/User/Email/Verification")
    suspend fun emailVerification(): Response<Unit?>

    @PATCH("/pgk63/api/User/Settings/Language")
    suspend fun updateLanguage(@Query("languageId") languageId: Int): Response<Unit?>

    @GET("/pgk63/api/User/Telegram/Token")
    suspend fun getTelegramToken(): Response<String>
}