package ru.pgk63.pgk.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import ru.pgk63.core_navigation.LocalNavHostController
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.feature_main.navigation.MainDestination

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(
    startDestination:String = MainDestination.route
) {
    val navHostController = LocalNavHostController.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PgkTheme.colors.primaryBackground
    ) {
        AnimatedNavHost(
            navController = navHostController,
            startDestination = startDestination,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = {300},
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = {-300},
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            route = "main_route",
            builder = {
                mainNavGraphBuilder(navController = navHostController)
            }
        )
    }
}