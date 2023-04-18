package ru.pgk63.feature_department.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.feature_department.screens.createDepartmentScreen.CreateDepartmentRoute
import ru.pgk63.feature_department.screens.departmentDetailsScreen.DepartmentDetailsRoute
import ru.pgk63.feature_department.screens.departmentListScreen.DepartmentListRoute

object DepartmentListDestination : NavigationDestination {
    override val route = "department_list_screen"
}

object DepartmentDetailsDestination : NavigationDestination {
    override val route: String = "department_details_screen"
    const val id_argument = "id"
}

object CreateDepartmentDestination : NavigationDestination {
    override val route: String = "create_department_screen"
    const val departmentHeadId = "departmentHeadId"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.departmentNavigation(
    onBackScreen: () -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onCreateDepartmentScreen: (departmentHeadId: Int?) -> Unit
) {
    composable(
        route = DepartmentListDestination.route
    ){
        DepartmentListRoute(
            onBackScreen = onBackScreen,
            onDepartmentDetailsScreen = onDepartmentDetailsScreen,
            onCreateDepartmentScreen = onCreateDepartmentScreen
        )
    }

    composable(
        route = "${DepartmentDetailsDestination.route}/{${DepartmentDetailsDestination.id_argument}}",
        arguments = listOf(
            navArgument(DepartmentDetailsDestination.id_argument){
                type = NavType.IntType
            }
        )
    ) {
        DepartmentDetailsRoute(
            departmentId = it.arguments!!.getInt(DepartmentDetailsDestination.id_argument),
            onBackScreen = onBackScreen,
            onGroupDetailsScreen = onGroupDetailsScreen,
            onSpecializationDetailsScreen = onSpecializationDetailsScreen
       )
    }

    composable(
        route = "${CreateDepartmentDestination.route}?" +
                "${CreateDepartmentDestination.departmentHeadId}={${CreateDepartmentDestination.departmentHeadId}}",
        arguments = listOf(
            navArgument(CreateDepartmentDestination.departmentHeadId){
                type = NavType.StringType
                nullable = true
            }
        )
    ){
        CreateDepartmentRoute(
            departmentHeadId = it.arguments?.getString(CreateDepartmentDestination.departmentHeadId)?.toIntOrNull(),
            onBackScreen = onBackScreen,
            onDepartmentDetailsScreen = onDepartmentDetailsScreen
        )
    }
}