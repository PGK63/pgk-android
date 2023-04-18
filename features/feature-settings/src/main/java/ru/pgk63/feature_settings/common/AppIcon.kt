package ru.pgk63.feature_settings.common

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.icon.ResIcons


internal enum class AppIconType(
    val nameAlias: String,
    @StringRes val title: Int,
    @DrawableRes val resIcon: Int
) {
    DEFAULT("ru.pgk63.pgk.activity.MainActivity",R.string.app_icon_default, ResIcons.pgkIcon),
    FEBRUARY_23("ru.pgk63.pgk.february_23_alias",R.string.app_icon_23_february, ResIcons.pgkIcon23February),
    MARTA_8("ru.pgk63.pgk.marta_8_alias",R.string.app_icon_8_marta, ResIcons.pgkIcon8Marta),
    FLAG("ru.pgk63.pgk.flag_alias",R.string.app_icon_flag, ResIcons.pgkIconFlag),
    NEW_YEAR("ru.pgk63.pgk.new_year_alias",R.string.app_icon_new_year, ResIcons.pgkIconNewYear)
}

internal class AppIcon(context: Context) {

    private val packageManager = context.packageManager
    private val activity = context as Activity
    private val sharedPreferences = context.getSharedPreferences(APP_ICON_SHARED_PREF_KEY, Context.MODE_PRIVATE)

    private companion object {
        const val APP_ICON_SHARED_PREF_KEY = "app_icon"
        const val TYPE_APP_ICON_KEY = "type_app_icon"
    }

    fun change(type: AppIconType) {
        try {
            val currentTypeIcon = getTypeIcon()
            disableIcon(currentTypeIcon)

            packageManager.setComponentEnabledSetting(
                ComponentName(activity, type.nameAlias),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            saveTypeIcon(type)
        }catch (e:Exception){
            Log.e("app_icon_change", e.toString())
        }
    }

    fun isCurrentTypeIcon(type: AppIconType): Boolean {
        val currentTypeIcon = getTypeIcon()
        return type == currentTypeIcon
    }

    private fun disableIcon(type: AppIconType) {
        packageManager.setComponentEnabledSetting(
            ComponentName(
                activity,
                type.nameAlias
            ),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun saveTypeIcon(type: AppIconType){
        sharedPreferences.edit()
            .putString(TYPE_APP_ICON_KEY, type.name)
            .apply()
    }

    fun getTypeIcon(): AppIconType {
        val typeIcon = sharedPreferences.getString(TYPE_APP_ICON_KEY, AppIconType.DEFAULT.name)
            ?: return AppIconType.DEFAULT

        return enumValueOf(typeIcon)
    }
}