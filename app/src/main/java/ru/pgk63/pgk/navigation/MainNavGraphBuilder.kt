package ru.pgk63.pgk.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.feature_guide.navigation.*
import com.example.feature_student.navigation.StudentDetailsDestination
import com.example.feature_student.navigation.StudentListDestination
import com.example.feature_student.navigation.studentNavigation
import ru.lfybkf19.feature_journal.navigation.*
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.feature_auth.navigation.ForgotPasswordDestination
import ru.pgk63.feature_auth.navigation.RegistrationUserDestination
import ru.pgk63.feature_auth.navigation.authNavigation
import ru.pgk63.feature_department.navigation.CreateDepartmentDestination
import ru.pgk63.feature_department.navigation.DepartmentDetailsDestination
import ru.pgk63.feature_department.navigation.DepartmentListDestination
import ru.pgk63.feature_department.navigation.departmentNavigation
import ru.pgk63.feature_group.navigation.CreateGroupDestination
import ru.pgk63.feature_group.navigation.GroupListDestination
import ru.pgk63.feature_group.navigation.GroupDetailsDestination
import ru.pgk63.feature_group.navigation.groupNavigation
import ru.pgk63.feature_main.navigation.NotificationListDestination
import ru.pgk63.feature_main.navigation.SearchDestination
import ru.pgk63.feature_main.navigation.mainNavigation
import ru.pgk63.feature_profile.navigation.ProfileDestination
import ru.pgk63.feature_profile.navigation.ProfileUpdateDestination
import ru.pgk63.feature_profile.navigation.profileNavigation
import ru.pgk63.feature_raportichka.navigation.RaportichkaAddRowDestination
import ru.pgk63.feature_raportichka.navigation.RaportichkaListDestination
import ru.pgk63.feature_raportichka.navigation.RaportichkaSortingDestination
import ru.pgk63.feature_raportichka.navigation.raportichkaNavigation
import ru.pgk63.feature_settings.navigation.*
import ru.pgk63.feature_specialization.navigation.CreateSpecializationDestination
import ru.pgk63.feature_specialization.navigation.SpecializationDetailsDestination
import ru.pgk63.feature_specialization.navigation.SpecializationListDestination
import ru.pgk63.feature_specialization.navigation.specializationNavigation
import ru.pgk63.feature_subject.navigation.SubjectDetailsDestination
import ru.pgk63.feature_subject.navigation.SubjectListDestination
import ru.pgk63.feature_subject.navigation.subjectNavigation
import ru.pgk63.feature_tech_support.navigation.TechSupportChatDestination
import ru.pgk63.feature_tech_support.navigation.TechSupportChatListDestination
import ru.pgk63.feature_tech_support.navigation.techSupportNavigation
import ru.pgk63.pgk.screens.updateAppScreen.UpdateAppDestination
import ru.pgk63.pgk.screens.updateAppScreen.updateAppNavigation

fun NavGraphBuilder.mainNavGraphBuilder(
    navController: NavController
){
    mainNavigation(
        onBackScreen = { navController.navigateUp() },
        onNotificationListScreen = {
            navController.navigate(NotificationListDestination.route)
        },
        onTechSupportChatScreen = { userRole ->
            navController.navigate(
                if(userRole != UserRole.ADMIN)
                    TechSupportChatDestination.route
                else
                    TechSupportChatListDestination.route
            )
        },
        onGroupScreen = {
            navController.navigate(GroupListDestination.route)
        },
        onSpecializationListScreen = {
            navController.navigate(SpecializationListDestination.route)
        },
        onSubjectListScreen = {
            navController.navigate(SubjectListDestination.route)
        },
        onStudentListScreen = {
            navController.navigate(StudentListDestination.route)
        },
        onProfileScreen = {
            navController.navigate(ProfileDestination.route)
        },
        onDepartmentListScreen = {
            navController.navigate(DepartmentListDestination.route)
        },
        onRaportichkaScreen = { userRole, userId ->
            if(userRole != UserRole.STUDENT && userRole != UserRole.HEADMAN &&userRole != UserRole.DEPUTY_HEADMAN){
                navController.navigate(RaportichkaSortingDestination.route)
            }else {
                navController.navigate(
                    RaportichkaListDestination.route +
                            "?${RaportichkaListDestination.studentIds}=${listOf(userId)}"
                )
            }
        },
        onJournalScreen = { _, _, groupId ->
            if(groupId != null){
                navController.navigate(
                    "${JournalListDestination.route}?" +
                            "${JournalListDestination.groupId}=$groupId"
                )
            }else{
                navController.navigate(JournalListDestination.route)
            }
        },
        onGuideListScreen = {
            navController.navigate(GuideListDestination.route)
        },
        onSettingsScreen = {
            navController.navigate(SettingsDestination.route)
        },
        onSearchScreen = {
            navController.navigate(SearchDestination.route)
        },
        onStudentDetailsScreen = {
            navController.navigate("${StudentDetailsDestination.route}/$it")
        },
        onTeacherDetailsScreen = {
            navController.navigate("${TeacherDetailsDestination.route}/$it")
        },
        onDepartmentHeadDetailsScreen = {
            navController.navigate("${DepartmentHeadDetailsDestination.route}/$it")
        },
        onDirectorDetailsScreen = {
            navController.navigate("${DirectorDetailsDestination.route}/$it")
        },
        onDepartmentDetailsScreen = {
            navController.navigate("${DepartmentDetailsDestination.route}/$it")
        },
        onGroupDetailsScreen = {
            navController.navigate("${GroupDetailsDestination.route}/$it")
        },
        onSpecializationDetailsScreen = {
            navController.navigate("${SpecializationDetailsDestination.route}/$it")
        },
        onSubjectDetailsScreen = {
            navController.navigate("${SubjectDetailsDestination.route}/$it")
        }
    )

    updateAppNavigation(
        onBackScreen = { navController.navigateUp() }
    )

    authNavigation(
        onBackScreen = { navController.navigateUp() },
        onForgotPasswordScreen = {
            navController.navigate(ForgotPasswordDestination.route)
        }
    )

    groupNavigation(
        onGroupDetailsScreen = { id, inclusive ->
            navController.navigate("${GroupDetailsDestination.route}/$id"){
                popUpTo("${GroupDetailsDestination.route}/$id"){
                    this.inclusive = inclusive
                }
            }
        },
        onStudentDetailsScreen = { id ->
            navController.navigate("${StudentDetailsDestination.route}/$id")
        },
        onDepartmentDetailsScreen = { id ->
            navController.navigate("${DepartmentDetailsDestination.route}/$id")
        },
        onSpecializationDetailsScreen = { id ->
            navController.navigate("${SpecializationDetailsDestination.route}/$id")
        },
        onRegistrationHeadman = { groupId, deputy ->
            val userRole = if(deputy) UserRole.DEPUTY_HEADMAN else UserRole.HEADMAN

            navController.navigate("${RegistrationUserDestination.route}?" +
                    "${RegistrationUserDestination.groupId}=${groupId}&" +
                    "${RegistrationUserDestination.userRole}=$userRole"
            )
        },
        onCreateGroupScreen = {
            navController.navigate(CreateGroupDestination.route)
        },
        onRegistrationStudentScreen = { groupId ->
            navController.navigate(
                "${RegistrationUserDestination.route}?" +
                        "${RegistrationUserDestination.userRole}=${UserRole.STUDENT}" +
                        "&${RegistrationUserDestination.groupId}=$groupId"
            )
        },
        onJournalScreen = { id, course, semester, group, groupId ->
            navController.navigate(
                "${JournalDetailsDestination.route}/$id?" +
                        "${JournalDetailsDestination.course}=$course&" +
                        "${JournalDetailsDestination.semester}=$semester&" +
                        "${JournalDetailsDestination.group}=$group&" +
                        "${JournalDetailsDestination.groupId}=$groupId"
            )
        },
        onCreateJournalScreen = { groupId ->
            navController.navigate(
                "${CreateJournalDestination.route}?${CreateJournalDestination.groupId}=$groupId"
            )
        },
        onTeacherDetailScreen = { teacherId ->
            navController.navigate("${TeacherDetailsDestination.route}/$teacherId")
        },
        onBackScreen = { navController.navigateUp() }
    )

    techSupportNavigation(
        onBackScreen = { navController.navigateUp() },
        onChatScreen = { chatId ->
            navController.navigate("${TechSupportChatDestination.route}?" +
                    "${TechSupportChatDestination.chatId}=$chatId")
        }
    )

    specializationNavigation(
        onBackScreen = { navController.navigateUp() },
        onSpecializationDetailsScreen = { id ->
            navController.navigate("${SpecializationDetailsDestination.route}/$id")
        },
        onGroupDetailsScreen = { id ->
            navController.navigate("${GroupDetailsDestination.route}/$id")
        },
        onDepartmentDetailsScreen = { id ->
            navController.navigate("${DepartmentDetailsDestination.route}/$id")
        },
        onCreateSpecializationScreen = { departmentId ->
            if(departmentId == null){
                navController.navigate(CreateSpecializationDestination.route)
            }else{
                navController.navigate(
                    "${CreateSpecializationDestination.route}?" +
                            "${CreateSpecializationDestination.departmentId}={$departmentId}"
                )
            }
        }
    )

    subjectNavigation(
        onBackScreen = { navController.navigateUp() },
        onSubjectDetailsScreen = { id ->
            navController.navigate("${SubjectDetailsDestination.route}/$id")
        },
        onTeacherDetailsScreen = { id ->
            navController.navigate("${TeacherDetailsDestination.route}/$id")
        }
    )

    studentNavigation(
        onBackScreen = { navController.navigateUp() },
        onStudentDetailsScreen = { id ->
            navController.navigate("${StudentDetailsDestination.route}/$id")
        },
        onGroupDetailsScreen = { id ->
            navController.navigate("${GroupDetailsDestination.route}/$id")
        }
    )

    profileNavigation(
        onBackScreen = { navController.navigateUp() },
        onProfileUpdateScreen = { type ->
            navController.navigate(
                "${ProfileUpdateDestination.route}?${ProfileUpdateDestination.type}=$type"
            )
        },
        onUserPageScreen = { userRole, userId ->
            when (userRole) {
                UserRole.STUDENT -> {
                    navController.navigate("${StudentDetailsDestination.route}/$userId")
                }
                UserRole.TEACHER -> {
                    navController.navigate("${TeacherDetailsDestination.route}/$userId")
                }
                UserRole.DEPARTMENT_HEAD -> {
                    navController.navigate("${DepartmentHeadDetailsDestination.route}/$userId")
                }
                UserRole.DIRECTOR -> {
                    navController.navigate("${DirectorDetailsDestination.route}/$id")
                }
                else -> Unit
            }
        },
        onSettingsEmailScreen = {
            navController.navigate(SettingsEmailDestination.route)
        },
        onSettingsTelegramScreen = {
            navController.navigate(SettingsTelegramDestination.route)
        }
    )

    departmentNavigation(
        onBackScreen = { navController.navigateUp() },
        onDepartmentDetailsScreen = { id ->
            navController.navigate("${DepartmentDetailsDestination.route}/$id")
        },
        onSpecializationDetailsScreen = { id ->
            navController.navigate("${SpecializationDetailsDestination.route}/$id")
        },
        onGroupDetailsScreen = { id ->
            navController.navigate("${GroupDetailsDestination.route}/$id")
        },
        onCreateDepartmentScreen = { departmentHeadId ->
            if(departmentHeadId == null){
                navController.navigate(CreateDepartmentDestination.route)
            }else{
                navController.navigate(
                    "${CreateDepartmentDestination.route}?" +
                            "${CreateDepartmentDestination.departmentHeadId}={$departmentHeadId}"
                )
            }
        }
    )

    raportichkaNavigation(
        onBackScreen = { navController.navigateUp() },
        onStudentDetailsScreen = { studentId ->
            navController.navigate("${StudentDetailsDestination.route}/$studentId")
        },
        onTeacherDetailsScreen = { teacherId ->
            navController.navigate("${TeacherDetailsDestination.route}/$teacherId")
        },
        onSubjectDetailsScreen = { subjectId ->
            navController.navigate("${SubjectDetailsDestination.route}/$subjectId")
        },
        onRaportichkaScreen = { studentId, groupsId, subjectsId, teacherId, startDate, endDate, onlyDate ->
            navController.navigate(
                RaportichkaListDestination.route +
                        "?${RaportichkaListDestination.studentIds}=$studentId" +
                        "&${RaportichkaListDestination.groupIds}=$groupsId" +
                        "&${RaportichkaListDestination.subjectIds}=$subjectsId" +
                        "&${RaportichkaListDestination.teacherIds}=$teacherId" +
                        "&${RaportichkaListDestination.startDate}=$startDate" +
                        "&${RaportichkaListDestination.endDate}=$endDate" +
                        "&${RaportichkaListDestination.onlyDate}=$onlyDate"
            )
        },
        onRaportichkaAddRowScreen = { raportichkaId, groupId ->
            navController.navigate(
                "${RaportichkaAddRowDestination.route}/$raportichkaId?" +
                        "${RaportichkaAddRowDestination.groupId}=$groupId"
            )
        },

        onRaportichkaUpdateRowScreen = { raportichkaId, groupId, rowId, updateTeacherId,
                                         updateNumberLesson, updateCountHours, updateStudentId, updateSubjectId ->
            navController.navigate(
                "${RaportichkaAddRowDestination.route}/$raportichkaId?" +
                        "${RaportichkaAddRowDestination.groupId}=$groupId" +
                        "&${RaportichkaAddRowDestination.raportichkaRowId}=$rowId" +
                        "&${RaportichkaAddRowDestination.updateTeacherId}=$updateTeacherId" +
                        "&${RaportichkaAddRowDestination.updateNumberLesson}=$updateNumberLesson" +
                        "&${RaportichkaAddRowDestination.updateCountHours}=$updateCountHours" +
                        "&${RaportichkaAddRowDestination.updateStudentId}=$updateStudentId" +
                        "&${RaportichkaAddRowDestination.updateSubjectId}=$updateSubjectId"
            )
        }
    )

    journalNavigation(
        onBackScreen = {
            navController.navigateUp()
        },
        onJournalDetailsScreen = { journalId, course, semester, group,
                                   groupId, journalSubjectId, subjectTitle,
                                   subjectTeacher, subjectHorse, subjectTeacherId ->

            navController.navigate(
                "${JournalDetailsDestination.route}/$journalId?" +
                        "${JournalDetailsDestination.course}=$course&" +
                        "${JournalDetailsDestination.semester}=$semester&" +
                        "${JournalDetailsDestination.group}=$group&" +
                        "${JournalDetailsDestination.groupId}=$groupId&" +
                        "${JournalDetailsDestination.journalSubjectId}=$journalSubjectId&" +
                        "${JournalDetailsDestination.subjectTitle}=$subjectTitle&" +
                        "${JournalDetailsDestination.subjectTeacher}=$subjectTeacher&" +
                        "${JournalDetailsDestination.subjectHorse}=$subjectHorse&" +
                        "${JournalDetailsDestination.subjectTeacherId}=$subjectTeacherId"
            )
        },
        onJournalSubjectListScreen = { journalId, course, semester, group, groupId ->
            navController.navigate(
                "${JournalSubjectListDestination.route}/$journalId?" +
                        "${JournalSubjectListDestination.course}=$course&" +
                        "${JournalSubjectListDestination.semester}=$semester&" +
                        "${JournalSubjectListDestination.group}=$group&" +
                        "${JournalSubjectListDestination.groupId}=$groupId"
            )
        },
        onJournalTopicTableScreen = { journalSubjectId, maxSubjectHours, teacherId ->
            navController.navigate(
                "${JournalTopicTableDestination.route}/$journalSubjectId" +
                        "?${JournalTopicTableDestination.maxSubjectHours}=$maxSubjectHours" +
                        "&${JournalTopicTableDestination.teacherId}=$teacherId"
            )
        },
        onCreateJournalSubjectScreen = { journalId ->
            navController.navigate(
                "${CreateJournalSubjectDestination.route}?" +
                        "${CreateJournalSubjectDestination.journalId}=$journalId"
            )
        },
        onStudentDetailsScreen = { id ->
            navController.navigate("${StudentDetailsDestination.route}/${id}")
        }
    )

    guideNavigation(
        onBackScreen = { navController.navigateUp() },
        onDirectorDetailsScreen = { id ->
            navController.navigate("${DirectorDetailsDestination.route}/$id")
        },
        onDepartmentHeadDetailsScreen = { id ->
            navController.navigate("${DepartmentHeadDetailsDestination.route}/$id")
        },
        onTeacherDetailsScreen = { id ->
            navController.navigate("${TeacherDetailsDestination.route}/$id")
        },
        onGroupDetailsScreen = { id ->
            navController.navigate("${GroupDetailsDestination.route}/$id")
        },
        onSubjectDetailsScreen = { id ->
            navController.navigate("${SubjectDetailsDestination.route}/$id")
        },
        onRegistrationScreen = { userRole ->
            navController.navigate(
                "${RegistrationUserDestination.route}?" +
                        "${RegistrationUserDestination.userRole}=$userRole"
            )
        },
        onTeacherAddSubjectScreen = { teacherId ->
            navController.navigate("${TeacherAddSubjectDestination.route}/$teacherId")
        }
    )

    settingsNavigation(
        onBackScreen = { navController.navigateUp() },
        onSettingsSecurityScreen = {
            navController.navigate(SettingsSecurityDestination.route)
        },
        onSettingsNotificationsScreen = {
            navController.navigate(SettingsNotificationsDestination.route)
        },
        onSettingsLanguageScreen = {
            navController.navigate(SettingsLanguageDestination.route)
        },
        onSettingsAppearanceScreen = {
            navController.navigate(SettingsAppearanceDestination.route)
        },
        onChangePasswordScreen = {
            navController.navigate(ChangePasswordDestination.route)
        },
        onSettingsEmailScreen = {
            navController.navigate(SettingsEmailDestination.route)
        },
        onSettingsTelegramScreen = {
            navController.navigate(SettingsTelegramDestination.route)
        },
        onSettingsStorageUsageScreen = {
            navController.navigate(SettingsStorageUsageDestination.route)
        },
        onUpdateAppScreen = {
            navController.navigate(UpdateAppDestination.route)
        },
        onAboutAppScreen = {
            navController.navigate(AboutAppDestination.route)
        }
    )
}