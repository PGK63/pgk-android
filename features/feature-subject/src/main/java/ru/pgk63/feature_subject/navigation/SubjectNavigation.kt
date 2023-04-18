package ru.pgk63.feature_subject.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.feature_subject.screens.subjectDetailsScreen.SubjectDetailsRoute
import ru.pgk63.feature_subject.screens.subjectListScreen.SubjectListRoute

object SubjectListDestination : NavigationDestination {
    override val route = "subject_list_screen"
}

object SubjectDetailsDestination : NavigationDestination {
    override val route: String = "subject_details_screen"
    const val id_argument = "id"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.subjectNavigation(
    onBackScreen: () -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit
) {
    composable(
        route = SubjectListDestination.route
    ){
        SubjectListRoute(
            onBackScreen = onBackScreen,
            onSubjectDetailsScreen = onSubjectDetailsScreen
        )
    }

    composable(
        route = "${SubjectDetailsDestination.route}/{${SubjectDetailsDestination.id_argument}}",
        arguments = listOf(
            navArgument(SubjectDetailsDestination.id_argument){
                type = NavType.IntType
            }
        )
    ) {
        SubjectDetailsRoute(
            subjectId = it.arguments!!.getInt(SubjectDetailsDestination.id_argument),
            onBackScreen = onBackScreen,
            onTeacherDetailsScreen = onTeacherDetailsScreen
        )
    }
}