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
import ru.lfybkf19.feature_journal.screens.journalSubjectListScreen.JournalSubjectListRoute
import ru.lfybkf19.feature_journal.screens.journalTopicTableScreen.JournalTopicTableRoute
import ru.pgk63.core_navigation.NavigationDestination

object JournalListDestination : NavigationDestination {
    override val route = "journal_list_screen"
    const val groupId = "groupId"
}

typealias onJournalSubjectListScreen = (
    journalId: Int,
    course: Int,
    semester: Int,
    group: String,
    groupId: Int,
) -> Unit

object JournalSubjectListDestination : NavigationDestination {
    override val route = "journal_subject_list_screen"
    const val journalId = "journalId"
    const val course = "course"
    const val semester = "semester"
    const val group = "group"
    const val groupId = "groupId"
}

typealias onJournalDetailsScreen = (
    journalId: Int,
    course: Int,
    semester: Int,
    group: String,
    groupId: Int,
    journalSubjectId: Int,
    subjectTitle: String,
    subjectTeacher: String,
    subjectHorse: Int,
    subjectTeacherId: Int,
) -> Unit

object JournalDetailsDestination : NavigationDestination {
    override val route = "journal_details_screen"
    const val journalId = "journalId"
    const val course = "course"
    const val semester = "semester"
    const val group = "group"
    const val groupId = "groupId"
    const val journalSubjectId = "journalSubjectId"
    const val subjectTitle = "subjectTitle"
    const val subjectTeacher = "subjectTeacher"
    const val subjectHorse = "subjectHorse"
    const val subjectTeacherId = "subjectTeacherId"
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
    onJournalDetailsScreen: onJournalDetailsScreen,
    onJournalTopicTableScreen: (journalSubjectId:Int, maxSubjectHours:Int, teacherId: Int) -> Unit,
    onCreateJournalSubjectScreen: (journalId:Int) -> Unit,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onJournalSubjectListScreen: onJournalSubjectListScreen,
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
            onJournalSubjectListScreen = onJournalSubjectListScreen
        )
    }

    composable(
        route = "${JournalDetailsDestination.route}/{${JournalDetailsDestination.journalId}}?" +
                "${JournalDetailsDestination.course}={${JournalDetailsDestination.course}}" +
                "&${JournalDetailsDestination.semester}={${JournalDetailsDestination.semester}}" +
                "&${JournalDetailsDestination.group}={${JournalDetailsDestination.group}}" +
                "&${JournalDetailsDestination.groupId}={${JournalDetailsDestination.groupId}}" +
                "&${JournalDetailsDestination.journalSubjectId}={${JournalDetailsDestination.journalSubjectId}}" +
                "&${JournalDetailsDestination.subjectTitle}={${JournalDetailsDestination.subjectTitle}}" +
                "&${JournalDetailsDestination.subjectTeacher}={${JournalDetailsDestination.subjectTeacher}}" +
                "&${JournalDetailsDestination.subjectHorse}={${JournalDetailsDestination.subjectHorse}}" +
                "&${JournalDetailsDestination.subjectTeacherId}={${JournalDetailsDestination.subjectTeacherId}}",
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
            },
            navArgument(JournalDetailsDestination.journalSubjectId){
                type = NavType.IntType
            },
            navArgument(JournalDetailsDestination.subjectTitle){
                type = NavType.StringType
            },
            navArgument(JournalDetailsDestination.subjectTeacher){
                type = NavType.StringType
            },
            navArgument(JournalDetailsDestination.subjectHorse){
                type = NavType.IntType
            },
            navArgument(JournalDetailsDestination.subjectTeacherId){
                type = NavType.IntType
            },
        )
    ){
        JournalDetailsRoute(
            journalId = it.arguments?.getInt(JournalDetailsDestination.journalId)!!,
            journalSubjectId = it.arguments?.getInt(JournalDetailsDestination.journalSubjectId)!!,
            subjectTitle = it.arguments?.getString(JournalDetailsDestination.subjectTitle)!!,
            subjectTeacher = it.arguments?.getString(JournalDetailsDestination.subjectTeacher)!!,
            subjectHorse = it.arguments?.getInt(JournalDetailsDestination.subjectHorse)!!,
            subjectTeacherId = it.arguments?.getInt(JournalDetailsDestination.subjectTeacherId)!!,
            course = it.arguments?.getInt(JournalDetailsDestination.course)!!,
            semester = it.arguments?.getInt(JournalDetailsDestination.semester)!!,
            group = it.arguments?.getString(JournalDetailsDestination.group)!!,
            groupId = it.arguments?.getInt(JournalDetailsDestination.groupId)!!,
            onJournalTopicTableScreen = onJournalTopicTableScreen,
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
            onJournalSubjectListScreen = onJournalSubjectListScreen
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

    composable(
        route = "${JournalSubjectListDestination.route}/{${JournalSubjectListDestination.journalId}}?" +
                "${JournalSubjectListDestination.course}={${JournalSubjectListDestination.course}}" +
                "&${JournalSubjectListDestination.semester}={${JournalSubjectListDestination.semester}}" +
                "&${JournalSubjectListDestination.group}={${JournalSubjectListDestination.group}}" +
                "&${JournalSubjectListDestination.groupId}={${JournalSubjectListDestination.groupId}}",
        arguments = listOf(
            navArgument(JournalSubjectListDestination.journalId){
                type = NavType.IntType
            },
            navArgument(JournalSubjectListDestination.course){
                type = NavType.IntType
            },
            navArgument(JournalSubjectListDestination.semester){
                type = NavType.IntType
            },
            navArgument(JournalSubjectListDestination.group){
                type = NavType.StringType
            },
            navArgument(JournalSubjectListDestination.groupId){
                type = NavType.IntType
            },
        )
    ){
        JournalSubjectListRoute(
            journalId = it.arguments!!.getInt(JournalSubjectListDestination.journalId),
            course = it.arguments?.getInt(JournalSubjectListDestination.course)!!,
            semester = it.arguments?.getInt(JournalSubjectListDestination.semester)!!,
            group = it.arguments?.getString(JournalSubjectListDestination.group)!!,
            groupId = it.arguments?.getInt(JournalSubjectListDestination.groupId)!!,
            onJournalDetailsScreen = onJournalDetailsScreen,
            onCreateJournalSubjectScreen = onCreateJournalSubjectScreen
        )
    }
}