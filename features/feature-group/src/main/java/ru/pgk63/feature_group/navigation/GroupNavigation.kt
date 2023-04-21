package ru.pgk63.feature_group.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.core_navigation.`typealias`.onJournalSubjectListScreen
import ru.pgk63.feature_group.screens.createGroupScreen.CreateGroupRoute
import ru.pgk63.feature_group.screens.groupDetailsScreen.GroupDetailsRoute
import ru.pgk63.feature_group.screens.groupListScreen.GroupListRoute

object GroupListDestination : NavigationDestination {
    override val route = "group_screen"
}

object GroupDetailsDestination : NavigationDestination {
    override val route: String = "group_details_screen"
    const val id_argument = "id"
}

object CreateGroupDestination : NavigationDestination {
    override val route: String = "create_group_screen"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.groupNavigation(
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int, inclusive: Boolean) -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onRegistrationHeadman: (groupId: Int,deputy: Boolean) -> Unit,
    onCreateGroupScreen: () -> Unit,
    onRegistrationStudentScreen: (groupId: Int) -> Unit,
    onJournalSubjectListScreen: onJournalSubjectListScreen,
    onCreateJournalScreen: (groupId: Int) -> Unit,
    onTeacherDetailScreen: (teacherId: Int) -> Unit
) {
    composable(
        route = GroupListDestination.route
    ){
        GroupListRoute(
            onBackScreen = onBackScreen,
            onGroupDetailsScreen = {
                onGroupDetailsScreen(it,false)
            },
            onCreateGroupScreen = onCreateGroupScreen
        )
    }

    composable(
        route = "${GroupDetailsDestination.route}/{${GroupDetailsDestination.id_argument}}",
        arguments = listOf(
            navArgument(GroupDetailsDestination.id_argument){
                type = NavType.IntType
            }
        )
    ) {
       GroupDetailsRoute(
           groupId = it.arguments!!.getInt(GroupDetailsDestination.id_argument),
           onBackScreen = onBackScreen,
           onStudentDetailsScreen = onStudentDetailsScreen,
           onDepartmentDetailsScreen = onDepartmentDetailsScreen,
           onSpecializationDetailsScreen = onSpecializationDetailsScreen,
           onRegistrationHeadman = onRegistrationHeadman,
           onRegistrationStudentScreen = onRegistrationStudentScreen,
           onJournalSubjectListScreen = onJournalSubjectListScreen,
           onCreateJournalScreen = onCreateJournalScreen,
           onTeacherDetailScreen = onTeacherDetailScreen
       )
    }

    composable(
        route = CreateGroupDestination.route
    ){
        CreateGroupRoute(
            onBackScreen = onBackScreen,
            onGroupDetailsScreen = {
                onGroupDetailsScreen(it,true)
            }
        )
    }
}