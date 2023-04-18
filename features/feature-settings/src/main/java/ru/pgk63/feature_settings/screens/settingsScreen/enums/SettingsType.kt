package ru.pgk63.feature_settings.screens.settingsScreen.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.icon.ResIcons

internal enum class SettingsType(
    @StringRes val textId: Int,
    @StringRes val bodyId: Int,
    @DrawableRes val iconId: Int,
) {
    APPEARANCE(R.string.appearance,R.string.appearance_settings_body,ResIcons.appearance),
    NOTIFICATIONS(R.string.notifications,R.string.notifications_settings_body,ResIcons.notifications),
    STORAGE_USAGE(R.string.storage, R.string.storage_settings_body, ResIcons.storage),
    SECURITY(R.string.security,R.string.security_settings_body,ResIcons.security),
    LANGUAGE(R.string.language,R.string.language_settings_body,ResIcons.language),
    ABOUT_APP(R.string.about_app, R.string.about_app_body, ResIcons.info)
}