package ru.lfybkf19.feature_journal.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import ru.lfybkf19.feature_journal.screens.createJournalScreen.CreateJournalRoute
import ru.lfybkf19.feature_journal.screens.createJournalSubject.CreateJournalSubjectRoute
import ru.lfybkf19.feature_journal.screens.journalDetailsScreen.JournalDetailsRoute
import ru.lfybkf19.feature_journal.screens.journalListScreen.JournalListRoute
import ru.lfybkf19.feature_journal.screens.journalTopicTableScreen.JournalTopicTableRoute
import ru.pgk63.core_navigation.NavigationDestination

object JournalListDestination : NavigationDestination {
    override val route = "journal_list_screen"
    const val groupId = "groupId"
}

object JournalDetailsDestination : NavigationDestination {
    override val route = "journal_details_screen"
    const val journalId = "journalId"
    const val course = "course"
    const val semester = "semester"
    const val group = "group"
    const val groupId = "groupId"
}

object JournalTopicTableDestination : NavigationDestination {
    override val route = "journal_topics_table_screen"
    const val journalSubjectId = "journalSubjectId"
    const val teacherId = "teacherId"
    const val maxSubjectHours = "maxSubjectHours"
}

object CreateJournalDestination : NavigationDestination {
    override val route = "create_journal_screen"
    const val groupId = "groupId"
}

object CreateJournalSubjectDestination : NavigationDestination {
    override val route = "create_journal_subject_screen"
    const val journalId = "journalId"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.journalNavigation(
    onJournalDetailsScreen: (
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
    ) -> Unit,
    onJournalTopicTableScreen: (journalSubjectId:Int, maxSubjectHours:Int, teacherId: Int) -> Unit,
    onCreateJournalSubjectScreen: (journalId:Int) -> Unit,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit
) {
    composable(
        route = "${JournalListDestination.route}?" +
                "${JournalListDestination.groupId}={${JournalListDestination.groupId}}",
        arguments = listOf(
            navArgument(JournalListDestination.groupId){
                type = NavType.StringType
                nullable = true
            }
        )
    ){
        JournalListRoute(
            groupId = it.arguments?.getString(JournalListDestination.groupId)?.toIntOrNull(),
            onBackScreen = onBackScreen,
            onJournalDetailsScreen = onJournalDetailsScreen
        )
    }

    composable(
        route = "${JournalDetailsDestination.route}/{${JournalDetailsDestination.journalId}}?" +
                "${JournalDetailsDestination.course}={${JournalDetailsDestination.course}}" +
                "&${JournalDetailsDestination.semester}={${JournalDetailsDestination.semester}}" +
                "&${JournalDetailsDestination.group}={${JournalDetailsDestination.group}}" +
                "&${JournalDetailsDestination.groupId}={${JournalDetailsDestination.groupId}}",
        arguments = listOf(
            navArgument(JournalDetailsDestination.journalId){
                type = NavType.IntType
            },
            navArgument(JournalDetailsDestination.course){
                type = NavType.IntType
            },
            navArgument(JournalDetailsDestination.semester){
                type = NavType.IntType
            },
            navArgument(JournalDetailsDestination.group){
                type = NavType.StringType
            },
            navArgument(JournalDetailsDestination.groupId){
                type = NavType.IntType
            }
        )
    ){
        JournalDetailsRoute(
            journalId = it.arguments?.getInt(JournalDetailsDestination.journalId)!!,
            course = it.arguments?.getInt(JournalDetailsDestination.course)!!,
            semester = it.arguments?.getInt(JournalDetailsDestination.semester)!!,
            group = it.arguments?.getString(JournalDetailsDestination.group)!!,
            groupId = it.arguments?.getInt(JournalDetailsDestination.groupId)!!,
            onJournalTopicTableScreen = onJournalTopicTableScreen,
            onCreateJournalSubjectScreen = onCreateJournalSubjectScreen,
            onBackScreen = onBackScreen,
            onStudentDetailsScreen = onStudentDetailsScreen
        )
    }

    composable(
        route = "${JournalTopicTableDestination.route}/{${JournalTopicTableDestination.journalSubjectId}}?" +
                "${JournalTopicTableDestination.teacherId}={${JournalTopicTableDestination.teacherId}}&" +
                "${JournalTopicTableDestination.maxSubjectHours}={${JournalTopicTableDestination.maxSubjectHours}}",
        arguments = listOf(
            navArgument(JournalTopicTableDestination.journalSubjectId){
                type = NavType.IntType
            },
            navArgument(JournalTopicTableDestination.teacherId){
                type = NavType.IntType
            },
            navArgument(JournalTopicTableDestination.maxSubjectHours){
                type = NavType.IntType
            }
        )
    ){
        JournalTopicTableRoute(
            journalSubjectId = it.arguments?.getInt(JournalTopicTableDestination.journalSubjectId)!!,
            teacherId = it.arguments?.getInt(JournalTopicTableDestination.teacherId)!!,
            maxSubjectHours = it.arguments?.getInt(JournalTopicTableDestination.maxSubjectHours)!!,
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = "${CreateJournalDestination.route}?" +
                "${CreateJournalDestination.groupId}={${CreateJournalDestination.groupId}}",
        arguments = listOf(
            navArgument(CreateJournalDestination.groupId){
                type = NavType.IntType
            }
        )
    ){
        CreateJournalRoute(
            groupId = it.arguments!!.getInt(CreateJournalDestination.groupId),
            onBackScreen = onBackScreen,
            onJournalDetailsScreen = onJournalDetailsScreen
        )
    }

    composable(
        route = "${CreateJournalSubjectDestination.route}?" +
                "${CreateJournalSubjectDestination.journalId}={${CreateJournalSubjectDestination.journalId}}",
        arguments = listOf(
            navArgument(CreateJournalSubjectDestination.journalId){
                type = NavType.IntType
            }
        )
    ){
        CreateJournalSubjectRoute(
            journalId = it.arguments!!.getInt(CreateJournalSubjectDestination.journalId),
            onBackScreen = onBackScreen
        )
    }
}