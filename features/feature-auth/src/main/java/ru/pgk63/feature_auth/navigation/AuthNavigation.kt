package ru.pgk63.feature_auth.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.feature_auth.screens.auth.AuthRoute
import ru.pgk63.feature_auth.screens.forgotPassword.ForgotPasswordRoute
import ru.pgk63.feature_auth.screens.registrationUser.RegistrationUserRoute

object AuthDestination : NavigationDestination {
    override val route = "auth_screen"
}

object ForgotPasswordDestination : NavigationDestination {
    override val route = "forgot_password_screen"
}

object RegistrationUserDestination : NavigationDestination {
    override val route = "registration_user_screen"
    const val userRole = "userRole"
    const val groupId = "groupId"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavigation(
    onBackScreen: () -> Unit,
    onForgotPasswordScreen: () -> Unit
) {
    composable(
        route = AuthDestination.route
    ){
        AuthRoute(
            onForgotPasswordScreen = onForgotPasswordScreen
        )
    }

    composable(ForgotPasswordDestination.route){
        ForgotPasswordRoute(
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = "${RegistrationUserDestination.route}?" +
                "${RegistrationUserDestination.userRole}={${RegistrationUserDestination.userRole}}&" +
                "${RegistrationUserDestination.groupId}={${RegistrationUserDestination.groupId}}",
        arguments = listOf(
            navArgument(RegistrationUserDestination.userRole){
                type = NavType.StringType
            },
            navArgument(RegistrationUserDestination.groupId) {
                type = NavType.StringType
                nullable = true
            }
        )
    ){
        RegistrationUserRoute(
            userRole = enumValueOf(it.arguments?.getString(RegistrationUserDestination.userRole)!!),
            groupId = it.arguments?.getString(RegistrationUserDestination.groupId)?.toIntOrNull(),
            onBackScreen = onBackScreen
        )
    }
}