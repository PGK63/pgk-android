package ru.pgk63.feature_specialization.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.feature_specialization.screens.createSpecializationScreen.CreateSpecializationRoute
import ru.pgk63.feature_specialization.screens.specializationDetailsScreen.SpecializationDetailsRoute
import ru.pgk63.feature_specialization.screens.specializationListScreen.SpecializationListRoute

object SpecializationListDestination : NavigationDestination {
    override val route = "specialization_list_screen"
}

object SpecializationDetailsDestination : NavigationDestination {
    override val route: String = "specialization_details_screen"
    const val id_argument = "id"
}

object CreateSpecializationDestination : NavigationDestination {
    override val route: String = "create_specialization_screen"
    const val departmentId = "departmentId"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.specializationNavigation(
    onBackScreen: () -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit,
    onCreateSpecializationScreen: (departmentId: Int?) -> Unit
) {
    composable(
        route = SpecializationListDestination.route
    ){
        SpecializationListRoute(
            onBackScreen = onBackScreen,
            onSpecializationDetailsScreen = onSpecializationDetailsScreen,
            onCreateSpecializationScreen = onCreateSpecializationScreen
        )
    }

    composable(
        route = "${SpecializationDetailsDestination.route}/{${SpecializationDetailsDestination.id_argument}}",
        arguments = listOf(
            navArgument(SpecializationDetailsDestination.id_argument){
                type = NavType.IntType
            }
        )
    ) {
       SpecializationDetailsRoute(
           specializationId = it.arguments!!.getInt(SpecializationDetailsDestination.id_argument),
           onBackScreen = onBackScreen,
           onGroupDetailsScreen = onGroupDetailsScreen,
           onDepartmentDetailsScreen = onDepartmentDetailsScreen
       )
    }

    composable(
        route = "${CreateSpecializationDestination.route}?" +
                "${CreateSpecializationDestination.departmentId}={${CreateSpecializationDestination.departmentId}}",
        arguments = listOf(
            navArgument(CreateSpecializationDestination.route){
                type = NavType.StringType
                nullable = true
            }
        )
    ){
        CreateSpecializationRoute(
            departmentId = it.arguments?.getString(CreateSpecializationDestination.departmentId)?.toIntOrNull(),
            onBackScreen = onBackScreen,
            onSpecializationDetailsScreen = onSpecializationDetailsScreen
        )
    }
}