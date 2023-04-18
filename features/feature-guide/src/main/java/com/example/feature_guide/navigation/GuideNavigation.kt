package com.example.feature_guide.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.feature_guide.screens.departmentHeadDetailsScreen.DepartmentHeadDetailsRoute
import com.example.feature_guide.screens.directorDetailsScreen.DirectorDetailsRoute
import com.example.feature_guide.screens.guideListScreen.GuideListRoute
import com.example.feature_guide.screens.teacherAddSubjectScreen.TeacherAddSubjectRoute
import com.example.feature_guide.screens.teacherDetailsScreen.TeacherDetailsRoute
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_navigation.NavigationDestination

object GuideListDestination : NavigationDestination {
    override val route: String = "guide_list_screen"
}

object DirectorDetailsDestination : NavigationDestination {
    override val route: String = "director_details_screen"
    const val directorId = "directorId"
}

object DepartmentHeadDetailsDestination : NavigationDestination {
    override val route: String = "department_head_screen"
    const val departmentHeadId = "departmentHeadId"
}

object TeacherDetailsDestination : NavigationDestination {
    override val route: String = "teacher_details_screen"
    const val teacherId = "teacherId"
}

object TeacherAddSubjectDestination : NavigationDestination {
    override val route: String = "teacher_add_subject_screen"
    const val teacherId = "teacherId"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.guideNavigation(
    onBackScreen: () -> Unit,
    onDirectorDetailsScreen: (directorId: Int) -> Unit,
    onDepartmentHeadDetailsScreen: (departmentHeadId: Int) -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onRegistrationScreen: (userRole: UserRole) -> Unit,
    onTeacherAddSubjectScreen: (teacherId: Int) -> Unit
) {
    composable(
        route = GuideListDestination.route
    ){
        GuideListRoute(
            onBackScreen = onBackScreen,
            onDirectorDetailsScreen = onDirectorDetailsScreen,
            onDepartmentHeadDetailsScreen = onDepartmentHeadDetailsScreen,
            onTeacherDetailsScreen = onTeacherDetailsScreen,
            onRegistrationScreen = onRegistrationScreen
        )
    }

    composable(
        route = "${DirectorDetailsDestination.route}/{${DirectorDetailsDestination.directorId}}",
        arguments = listOf(
            navArgument(DirectorDetailsDestination.directorId){
                type = NavType.StringType
            }
        )
    ){
        DirectorDetailsRoute(
            directorId = it.arguments?.getString(DirectorDetailsDestination.directorId)!!.toInt(),
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = "${DepartmentHeadDetailsDestination.route}/{${DepartmentHeadDetailsDestination.departmentHeadId}}",
        arguments = listOf(
            navArgument(DepartmentHeadDetailsDestination.departmentHeadId){
                type = NavType.StringType
            }
        )
    ){
        DepartmentHeadDetailsRoute(
            departmentHeadId = it.arguments?.getString(DepartmentHeadDetailsDestination.departmentHeadId)!!.toInt(),
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = "${TeacherDetailsDestination.route}/{${TeacherDetailsDestination.teacherId}}",
        arguments = listOf(
            navArgument(TeacherDetailsDestination.teacherId){
                type = NavType.StringType
            }
        )
    ){
        TeacherDetailsRoute(
            teacherId = it.arguments?.getString(TeacherDetailsDestination.teacherId)!!.toInt(),
            onBackScreen = onBackScreen,
            onSubjectDetailsScreen = onSubjectDetailsScreen,
            onGroupDetailsScreen = onGroupDetailsScreen,
            onTeacherAddSubjectScreen = onTeacherAddSubjectScreen
        )
    }

    composable(
        route = "${TeacherAddSubjectDestination.route}/{${TeacherAddSubjectDestination.teacherId}}",
        arguments = listOf(
            navArgument(TeacherAddSubjectDestination.teacherId){
                type = NavType.StringType
            }
        )
    ){
        TeacherAddSubjectRoute(
            teacherId = it.arguments?.getString(TeacherAddSubjectDestination.teacherId)!!.toInt(),
            onBackScreen = onBackScreen
        )
    }
}