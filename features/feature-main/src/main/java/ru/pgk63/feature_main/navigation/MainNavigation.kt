package ru.pgk63.feature_main.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.feature_main.screens.mainScreen.MainRoute
import ru.pgk63.feature_main.screens.notificationListScreen.NotificationListRoute
import ru.pgk63.feature_main.screens.searchScreen.SearchRoute

object MainDestination : NavigationDestination {
    override val route = "main_screen"
}

object NotificationListDestination : NavigationDestination {
    override val route: String = "notification_list_screen"
}

object SearchDestination : NavigationDestination {
    override val route: String = "search_screen"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mainNavigation(
    onBackScreen: () -> Unit,
    onNotificationListScreen: () -> Unit,
    onGroupScreen: () -> Unit,
    onTechSupportChatScreen: (userRole: UserRole) -> Unit,
    onSettingsScreen: () -> Unit,
    onSpecializationListScreen: () -> Unit,
    onSubjectListScreen: () -> Unit,
    onStudentListScreen: () -> Unit,
    onProfileScreen: () -> Unit,
    onDepartmentListScreen: () -> Unit,
    onRaportichkaScreen: (userRole: UserRole, userId: Int) -> Unit,
    onJournalScreen: (userRole: UserRole?, userId: Int?, groupId: Int?) -> Unit,
    onGuideListScreen: () -> Unit,
    onSearchScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit,
    onDepartmentHeadDetailsScreen: (departmentHeadId: Int) -> Unit,
    onDirectorDetailsScreen: (directorId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit
) {
    composable(
        route = MainDestination.route
    ){
        MainRoute(
            onNotificationListScreen = onNotificationListScreen,
            onGroupScreen = onGroupScreen,
            onTechSupportChatScreen = onTechSupportChatScreen,
            onSettingsScreen = onSettingsScreen,
            onSpecializationListScreen = onSpecializationListScreen,
            onSubjectListScreen = onSubjectListScreen,
            onStudentListScreen = onStudentListScreen,
            onProfileScreen = onProfileScreen,
            onDepartmentListScreen = onDepartmentListScreen,
            onRaportichkaScreen = onRaportichkaScreen,
            onJournalScreen = onJournalScreen,
            onGuideListScreen = onGuideListScreen,
            onSearchScreen = onSearchScreen,
            onStudentDetailsScreen = onStudentDetailsScreen,
            onTeacherDetailsScreen = onTeacherDetailsScreen,
            onDepartmentHeadDetailsScreen = onDepartmentHeadDetailsScreen,
            onDirectorDetailsScreen = onDirectorDetailsScreen,
            onDepartmentDetailsScreen = onDepartmentDetailsScreen,
            onGroupDetailsScreen = onGroupDetailsScreen,
            onSpecializationDetailsScreen = onSpecializationDetailsScreen,
            onSubjectDetailsScreen = onSubjectDetailsScreen
        )
    }

    composable(
        route = NotificationListDestination.route
    ){
        NotificationListRoute(
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = SearchDestination.route
    ){
        SearchRoute(
            onBackScreen = onBackScreen,
            onStudentDetailsScreen = onStudentDetailsScreen,
            onTeacherDetailsScreen = onTeacherDetailsScreen,
            onDepartmentHeadDetailsScreen = onDepartmentHeadDetailsScreen,
            onDirectorDetailsScreen = onDirectorDetailsScreen,
            onDepartmentDetailsScreen = onDepartmentDetailsScreen,
            onGroupDetailsScreen = onGroupDetailsScreen,
            onSpecializationDetailsScreen = onSpecializationDetailsScreen,
            onSubjectDetailsScreen = onSubjectDetailsScreen
        )
    }
}