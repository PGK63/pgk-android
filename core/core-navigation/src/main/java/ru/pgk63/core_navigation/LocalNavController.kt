package ru.pgk63.core_navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController

val LocalNavHostController = staticCompositionLocalOf<NavHostController> {
    error("No navController provided")
}

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("No navController provided")
}