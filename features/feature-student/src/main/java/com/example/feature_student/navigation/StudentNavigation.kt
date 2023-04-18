package com.example.feature_student.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.feature_student.screens.studentDetailsScreen.StudentDetailsRoute
import com.example.feature_student.screens.studentListScreen.StudentListRoute
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_navigation.NavigationDestination

object StudentListDestination : NavigationDestination {
    override val route = "student_list_screen"
}

object StudentDetailsDestination : NavigationDestination {
    override val route: String = "student_details_screen"
    const val id_argument = "id"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.studentNavigation(
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit
) {
    composable(
        route = StudentListDestination.route
    ){
        StudentListRoute(
            onBackScreen = onBackScreen,
            onStudentDetailsScreen = onStudentDetailsScreen
        )
    }

    composable(
        route = "${StudentDetailsDestination.route}/{${StudentDetailsDestination.id_argument}}",
        arguments = listOf(
            navArgument(StudentDetailsDestination.id_argument){
                type = NavType.IntType
            }
        )
    ) {
       StudentDetailsRoute(
           studentId = it.arguments!!.getInt(StudentDetailsDestination.id_argument),
           onBackScreen = onBackScreen,
           onGroupDetailsScreen = onGroupDetailsScreen
       )
    }
}