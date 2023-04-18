package ru.pgk63.feature_settings.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.feature_settings.screens.aboutAppScreen.AboutAppRoute
import ru.pgk63.feature_settings.screens.changePasswordScreen.ChangePasswordRoute
import ru.pgk63.feature_settings.screens.settingsAppearanceScreen.SettingsAppearanceRoute
import ru.pgk63.feature_settings.screens.settingsEmailScreen.SettingsEmailRoute
import ru.pgk63.feature_settings.screens.settingsLanguageScreen.SettingsLanguageScreenRoute
import ru.pgk63.feature_settings.screens.settingsNotificationsScreen.SettingsNotificationsRoute
import ru.pgk63.feature_settings.screens.settingsScreen.SettingsRoute
import ru.pgk63.feature_settings.screens.settingsSecurityScreen.SettingsSecurityRoute
import ru.pgk63.feature_settings.screens.settingsStorageUsageScreen.SettingsStorageUsageRoute
import ru.pgk63.feature_settings.screens.settingsTelegramScreen.SettingsTelegramRoute

object SettingsDestination : NavigationDestination {
    override val route = "settings_screen"
}

object SettingsSecurityDestination : NavigationDestination {
    override val route = "settings_security_screen"
}

object SettingsNotificationsDestination : NavigationDestination {
    override val route = "settings_notifications_screen"
}

object SettingsLanguageDestination : NavigationDestination {
    override val route = "settings_language_screen"
}

object SettingsAppearanceDestination : NavigationDestination {
    override val route = "settings_appearance_screen"
}

object ChangePasswordDestination : NavigationDestination {
    override val route = "change_password_screen"
}

object SettingsEmailDestination : NavigationDestination {
    override val route = "settings_email_screen"
}

object SettingsTelegramDestination : NavigationDestination {
    override val route = "settings_telegram_screen"
}

object SettingsStorageUsageDestination : NavigationDestination {
    override val route = "settings_storage_usage_screen"
}

object AboutAppDestination : NavigationDestination {
    override val route = "about_app_screen"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsNavigation(
    onSettingsSecurityScreen:() -> Unit,
    onSettingsNotificationsScreen:() -> Unit,
    onSettingsLanguageScreen:() -> Unit,
    onSettingsAppearanceScreen:() -> Unit,
    onBackScreen: () -> Unit,
    onChangePasswordScreen: () -> Unit,
    onSettingsEmailScreen: () -> Unit,
    onSettingsTelegramScreen: () -> Unit,
    onSettingsStorageUsageScreen: () -> Unit,
    onUpdateAppScreen: () -> Unit,
    onAboutAppScreen: () -> Unit
) {
    composable(
        route = SettingsDestination.route
    ){
        SettingsRoute(
            onBackScreen = onBackScreen,
            onSettingsSecurityScreen = onSettingsSecurityScreen,
            onSettingsNotificationsScreen = onSettingsNotificationsScreen,
            onSettingsLanguageScreen = onSettingsLanguageScreen,
            onSettingsAppearanceScreen = onSettingsAppearanceScreen,
            onSettingsStorageUsageScreen = onSettingsStorageUsageScreen,
            onAboutAppScreen = onAboutAppScreen
        )
    }

    composable(
        route = SettingsSecurityDestination.route
    ){
        SettingsSecurityRoute(
            onBackScreen = onBackScreen,
            onChangePasswordScreen = onChangePasswordScreen,
            onSettingsEmailScreen = onSettingsEmailScreen,
            onSettingsTelegramScreen = onSettingsTelegramScreen
        )
    }

    composable(
        route = SettingsNotificationsDestination.route
    ){
        SettingsNotificationsRoute(
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = SettingsLanguageDestination.route
    ){
        SettingsLanguageScreenRoute(
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = SettingsAppearanceDestination.route
    ){
        SettingsAppearanceRoute(
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = ChangePasswordDestination.route
    ){
        ChangePasswordRoute(
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = SettingsEmailDestination.route
    ){
        SettingsEmailRoute(
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = SettingsTelegramDestination.route
    ){
        SettingsTelegramRoute(
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = SettingsStorageUsageDestination.route
    ){
        SettingsStorageUsageRoute(
            onBackScreen = onBackScreen
        )
    }

    composable(AboutAppDestination.route){
        AboutAppRoute(
            onBackScreen = onBackScreen,
            onUpdateAppScreen = onUpdateAppScreen
        )
    }
}