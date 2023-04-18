package ru.pgk63.feature_raportichka.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_common.extension.isContentNull
import ru.pgk63.core_common.extension.toIntArrayOrNull
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.feature_raportichka.screens.raportichkaAddRow.RaportichkaAddRowRoute
import ru.pgk63.feature_raportichka.screens.raportichkaScreen.RaportichkaRoute
import ru.pgk63.feature_raportichka.screens.raportichkaSortingScreen.RaportichkaSortingRoute

object RaportichkaListDestination : NavigationDestination {
    override val route = "raportichka_list_screen"
    const val confirmation = "confirmation"
    const val onlyDate = "onlyDate"
    const val startDate = "startDate"
    const val endDate = "endDate"
    const val groupIds = "groupIds"
    const val subjectIds = "subjectIds"
    const val classroomTeacherIds = "classroomTeacherIds"
    const val numberLessons = "numberLessons"
    const val teacherIds = "teacherIds"
    const val studentIds = "studentIds"
}

object RaportichkaSortingDestination : NavigationDestination {
    override val route = "raportichka_sorting_screen"
}

object RaportichkaAddRowDestination : NavigationDestination {
    override val route: String = "raportichka_add_row_screen"
    const val raportichkaId = "raportichkaId"
    const val groupId = "groupId"
    const val raportichkaRowId = "raportichkaRowId"
    const val updateTeacherId = "updateTeacherId"
    const val updateNumberLesson = "updateNumberLesson"
    const val updateCountHours = "updateCountHours"
    const val updateSubjectId = "updateSubjectId"
    const val updateStudentId = "updateStudentId"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.raportichkaNavigation(
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId:Int) -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit,
    onRaportichkaAddRowScreen: (raportichkaId:Int, groupId:Int) -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onRaportichkaScreen: (
        studentId: List<Int>?,
        groupsId: List<Int>?,
        subjectsId: List<Int>?,
        teacherId: List<Int>?,
        startDate: String?,
        endDate: String?,
        onlyDate: String?
    ) -> Unit,
    onRaportichkaUpdateRowScreen: (
        raportichkaId: Int,
        groupId: Int,
        rowId: Int,
        updateTeacherId: Int,
        updateNumberLesson: String,
        updateCountHours: String,
        updateStudentId: Int,
        updateSubjectId: Int
    ) -> Unit
) {
    composable(
        route = "${RaportichkaListDestination.route}?" +
                "${RaportichkaListDestination.confirmation}={${RaportichkaListDestination.confirmation}}&" +
                "${RaportichkaListDestination.onlyDate}={${RaportichkaListDestination.onlyDate}}&" +
                "${RaportichkaListDestination.startDate}={${RaportichkaListDestination.startDate}}&" +
                "${RaportichkaListDestination.endDate}={${RaportichkaListDestination.endDate}}&" +
                "${RaportichkaListDestination.groupIds}={${RaportichkaListDestination.groupIds}}&" +
                "${RaportichkaListDestination.subjectIds}={${RaportichkaListDestination.subjectIds}}&" +
                "${RaportichkaListDestination.classroomTeacherIds}={${RaportichkaListDestination.classroomTeacherIds}}&" +
                "${RaportichkaListDestination.numberLessons}={${RaportichkaListDestination.numberLessons}}&" +
                "${RaportichkaListDestination.teacherIds}={${RaportichkaListDestination.teacherIds}}&" +
                "${RaportichkaListDestination.studentIds}={${RaportichkaListDestination.studentIds}}",
        arguments = listOf(
            navArgument(RaportichkaListDestination.confirmation){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaListDestination.onlyDate){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaListDestination.startDate){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaListDestination.endDate){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaListDestination.groupIds){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaListDestination.subjectIds){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaListDestination.classroomTeacherIds){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaListDestination.numberLessons){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaListDestination.teacherIds){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaListDestination.studentIds){
                type = NavType.StringType
                nullable = true
            }
        )
    ){
        RaportichkaRoute(
            onBackScreen = onBackScreen,
            confirmation = it.arguments?.getString(RaportichkaListDestination.confirmation)?.toBooleanStrictOrNull(),
            onlyDate = it.arguments?.getString(RaportichkaListDestination.onlyDate).isContentNull(),
            startDate = it.arguments?.getString(RaportichkaListDestination.startDate).isContentNull(),
            endDate = it.arguments?.getString(RaportichkaListDestination.endDate).isContentNull(),
            groupIds = it.arguments?.getString(RaportichkaListDestination.groupIds)?.toIntArrayOrNull(),
            subjectIds = it.arguments?.getString(RaportichkaListDestination.subjectIds)?.toIntArrayOrNull(),
            classroomTeacherIds = it.arguments?.getString(RaportichkaListDestination.classroomTeacherIds)?.toIntArrayOrNull(),
            numberLessons = it.arguments?.getString(RaportichkaListDestination.numberLessons)?.toIntArrayOrNull(),
            teacherIds = it.arguments?.getString(RaportichkaListDestination.teacherIds)?.toIntArrayOrNull(),
            studentIds = it.arguments?.getString(RaportichkaListDestination.studentIds)?.toIntArrayOrNull(),
            onStudentDetailsScreen = onStudentDetailsScreen,
            onTeacherDetailsScreen = onTeacherDetailsScreen,
            onRaportichkaAddRowScreen = onRaportichkaAddRowScreen,
            onRaportichkaUpdateRowScreen = onRaportichkaUpdateRowScreen,
            onSubjectDetailsScreen = onSubjectDetailsScreen
        )
    }

    composable(RaportichkaSortingDestination.route){
        RaportichkaSortingRoute(
            onBackScreen = onBackScreen,
            onRaportichkaScreen = onRaportichkaScreen
        )
    }

    composable(
        route = "${RaportichkaAddRowDestination.route}/{${RaportichkaAddRowDestination.raportichkaId}}?" +
                "${RaportichkaAddRowDestination.groupId}={${RaportichkaAddRowDestination.groupId}}" +
                "&${RaportichkaAddRowDestination.raportichkaRowId}={${RaportichkaAddRowDestination.raportichkaRowId}}" +
                "&${RaportichkaAddRowDestination.updateTeacherId}={${RaportichkaAddRowDestination.updateTeacherId}}" +
                "&${RaportichkaAddRowDestination.updateNumberLesson}={${RaportichkaAddRowDestination.updateNumberLesson}}" +
                "&${RaportichkaAddRowDestination.updateCountHours}={${RaportichkaAddRowDestination.updateCountHours}}" +
                "&${RaportichkaAddRowDestination.updateSubjectId}={${RaportichkaAddRowDestination.updateSubjectId}}" +
                "&${RaportichkaAddRowDestination.updateStudentId}={${RaportichkaAddRowDestination.updateStudentId}}",
        arguments = listOf(
            navArgument(RaportichkaAddRowDestination.raportichkaId){
                type = NavType.IntType
            },
            navArgument(RaportichkaAddRowDestination.groupId){
                type = NavType.IntType
            },
            navArgument(RaportichkaAddRowDestination.raportichkaRowId){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaAddRowDestination.updateTeacherId){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaAddRowDestination.updateNumberLesson){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaAddRowDestination.updateCountHours){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaAddRowDestination.updateSubjectId){
                type = NavType.StringType
                nullable = true
            },
            navArgument(RaportichkaAddRowDestination.updateStudentId){
                type = NavType.StringType
                nullable = true
            }
        )
    ){
        RaportichkaAddRowRoute(
            onBackScreen = onBackScreen,
            raportichkaId = it.arguments?.getInt(RaportichkaAddRowDestination.raportichkaId)!!.toInt(),
            groupId = it.arguments?.getInt(RaportichkaAddRowDestination.groupId)!!.toInt(),
            raportichkaRowId = it.arguments?.getString(RaportichkaAddRowDestination.raportichkaRowId)?.toIntOrNull(),
            updateTeacherId = it.arguments?.getString(RaportichkaAddRowDestination.updateTeacherId)?.toIntOrNull(),
            updateNumberLesson = it.arguments?.getString(RaportichkaAddRowDestination.updateNumberLesson),
            updateCountHours = it.arguments?.getString(RaportichkaAddRowDestination.updateCountHours),
            updateStudentId = it.arguments?.getString(RaportichkaAddRowDestination.updateStudentId)?.toIntOrNull(),
            updateSubjectId = it.arguments?.getString(RaportichkaAddRowDestination.updateSubjectId)?.toIntOrNull()
        )
    }
}