package ru.pgk63.feature_profile.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.feature_profile.screens.profileScreen.ProfileRoute
import ru.pgk63.feature_profile.screens.profileUpdateScreen.ProfileUpdateRoute
import ru.pgk63.feature_profile.screens.profileUpdateScreen.model.ProfileUpdateType

object ProfileDestination : NavigationDestination {
    override val route = "profile_screen"
}

object ProfileUpdateDestination : NavigationDestination {
    override val route = "profile_update_screen"
    const val type = "type"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.profileNavigation(
    onBackScreen: () -> Unit,
    onProfileUpdateScreen:(ProfileUpdateType) -> Unit,
    onUserPageScreen: (UserRole, userId: Int) -> Unit,
    onSettingsEmailScreen: () -> Unit,
    onSettingsTelegramScreen: () -> Unit,
) {
    composable(
        route = ProfileDestination.route
    ){
        ProfileRoute(
            onBackScreen = onBackScreen,
            onProfileUpdateScreen = onProfileUpdateScreen,
            onUserPageScreen = onUserPageScreen,
            onSettingsEmailScreen = onSettingsEmailScreen,
            onSettingsTelegramScreen = onSettingsTelegramScreen
        )
    }

    composable(
        route = "${ProfileUpdateDestination.route}?" +
                "${ProfileUpdateDestination.type}={${ProfileUpdateDestination.type}}",
        arguments = listOf(
            navArgument(ProfileUpdateDestination.type){
                type = NavType.StringType
            }
        )
    ){
        ProfileUpdateRoute(
            type = enumValueOf(it.arguments!!.getString(ProfileUpdateDestination.type)!!),
            onBackScreen = onBackScreen
        )
    }
}