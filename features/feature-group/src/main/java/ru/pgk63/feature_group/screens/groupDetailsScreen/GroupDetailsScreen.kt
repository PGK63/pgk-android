package ru.pgk63.feature_group.screens.groupDetailsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_model.journal.Journal
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.*
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.feature_group.screens.groupDetailsScreen.model.GroupDetailsMenu
import ru.pgk63.feature_group.screens.groupDetailsScreen.viewModel.GroupDetailsViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun GroupDetailsRoute(
    viewModel: GroupDetailsViewModel = hiltViewModel(),
    groupId: Int,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onRegistrationHeadman: (groupId: Int,deputy: Boolean) -> Unit,
    onRegistrationStudentScreen: (groupId: Int) -> Unit,
    onJournalScreen: (
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
    ) -> Unit,
    onCreateJournalScreen: (groupId: Int) -> Unit,
    onTeacherDetailScreen: (teacherId: Int) -> Unit
) {
    var groupResult by remember { mutableStateOf<Result<Group>>(Result.Loading()) }

    val students = viewModel.getStudentsByGroupId(id = groupId).collectAsLazyPagingItems()
    val journalList = viewModel.responseJournalList.collectAsLazyPagingItems()

    var user by remember { mutableStateOf(UserLocalDatabase()) }

    viewModel.responseLocalUser.onEach {
        user = it
    }.launchWhenStarted()

    viewModel.responseGroup.onEach { result ->
        groupResult = result
    }.launchWhenStarted()

    viewModel.responseDeleteGroupResult.onEach { result ->
        if(result is Result.Success){
            onBackScreen()
        }
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getGroupById(id = groupId)
        viewModel.getJournalList(groupId = groupId)
    })

    GroupDetailsScreen(
        groupResult = groupResult,
        user = user,
        journalList = journalList,
        onBackScreen = onBackScreen,
        students = students,
        onStudentDetailsScreen = onStudentDetailsScreen,
        onDepartmentDetailsScreen = onDepartmentDetailsScreen,
        onSpecializationDetailsScreen = onSpecializationDetailsScreen,
        onRegistrationStudentScreen = onRegistrationStudentScreen,
        onJournalScreen = onJournalScreen,
        onCreateJournalScreen = onCreateJournalScreen,
        onTeacherDetailScreen = onTeacherDetailScreen,
        onRegistrationHeadman = { deputy ->
            onRegistrationHeadman(groupId, deputy)
        },
        deleteGroup = {
            viewModel.deleteGroupById(groupId)
        }
    )
}

@Composable
private fun GroupDetailsScreen(
    groupResult: Result<Group>,
    user: UserLocalDatabase,
    students: LazyPagingItems<Student>,
    journalList: LazyPagingItems<Journal>,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onRegistrationHeadman: (deputy: Boolean) -> Unit,
    onRegistrationStudentScreen: (groupId: Int) -> Unit,
    onJournalScreen: (
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
    ) -> Unit,
    deleteGroup: () -> Unit,
    onCreateJournalScreen: (groupId: Int) -> Unit,
    onTeacherDetailScreen: (teacherId: Int) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()
    var mainMenuVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {

            val title = if(groupResult is Result.Success){
                val group = groupResult.data!!
                "${group.speciality.nameAbbreviation}-${group.course}${group.number}"
            }else { "" }

            TopBarBack(
                title = title,
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
                actions = {
                    Column {
                        IconButton(onClick = { mainMenuVisible = true }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = PgkTheme.colors.tintColor
                            )
                        }

                        MainMenu(
                            openMenu = mainMenuVisible,
                            closeMenu = { mainMenuVisible = false },
                            visibilityItem = { menu ->
                                when(menu){
                                    GroupDetailsMenu.ADD_STUDENT,
                                    GroupDetailsMenu.CREATE_JOURNAL,
                                    GroupDetailsMenu.DELETE_GROUP -> {
                                        user.userRole == UserRole.ADMIN
                                                || user.userRole == UserRole.EDUCATIONAL_SECTOR
                                                || (user.userRole == UserRole.TEACHER
                                                && groupResult.data?.classroomTeacher?.id == user.userId)
                                    }
                                    GroupDetailsMenu.ADD_HEADMAN,
                                    GroupDetailsMenu.ADD_DEPUTY_HEADMAN -> {
                                        user.userRole == UserRole.TEACHER
                                                && groupResult.data?.classroomTeacher?.id == user.userId
                                    }
                                }
                            }
                        ) { menu ->
                            when (menu) {
                                GroupDetailsMenu.ADD_HEADMAN -> onRegistrationHeadman(false)
                                GroupDetailsMenu.ADD_DEPUTY_HEADMAN -> onRegistrationHeadman(true)
                                GroupDetailsMenu.ADD_STUDENT -> {
                                    if (groupResult.data?.id != null) {
                                        onRegistrationStudentScreen(groupResult.data!!.id)
                                    }
                                }
                                GroupDetailsMenu.CREATE_JOURNAL -> {
                                    if (groupResult.data?.id != null) {
                                        onCreateJournalScreen(groupResult.data!!.id)
                                    }
                                }
                                GroupDetailsMenu.DELETE_GROUP -> deleteGroup()
                            }
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            when(groupResult){
                is Result.Error -> ErrorUi(message = groupResult.message)
                is Result.Loading -> LoadingUi()
                is Result.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            Column {
                                Spacer(modifier = Modifier.height(10.dp))

                                ClassroomTeacherUi(
                                    classroomTeacher = groupResult.data!!.classroomTeacher,
                                    onTeacherDetailScreen = onTeacherDetailScreen
                                )
                            }
                        }

                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            Column {
                                Spacer(modifier = Modifier.height(10.dp))

                                DepartmentAndSpecialityUi(
                                    department = groupResult.data!!.speciality.department,
                                    speciality = groupResult.data!!.speciality,
                                    onClickDepartment = {
                                        onDepartmentDetailsScreen(groupResult.data!!.speciality.department.id)
                                    },
                                    onClickSpecialization = {
                                        onSpecializationDetailsScreen(groupResult.data!!.speciality.id)
                                    }
                                )
                            }
                        }

                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            Column {
                                if(journalList.itemCount > 0 ){
                                    Spacer(modifier = Modifier.height(25.dp))

                                    Text(
                                        text = stringResource(id = R.string.journals),
                                        color = PgkTheme.colors.primaryText,
                                        style = PgkTheme.typography.heading,
                                        fontFamily = PgkTheme.fontFamily.fontFamily,
                                        modifier = Modifier.padding(start = 20.dp)
                                    )

                                    Spacer(modifier = Modifier.height(5.dp))

                                    LazyRow {
                                        items(journalList) { journal ->
                                            if(journal != null){
                                                JournalUi(
                                                    group = journal.group.toString(),
                                                    course = journal.course.toString(),
                                                    semester = journal.semester.toString(),
                                                    modifier = Modifier.padding(5.dp),
                                                    onClick = {
                                                        onJournalScreen(
                                                            journal.id,
                                                            journal.course,
                                                            journal.semester,
                                                            journal.group.toString(),
                                                            journal.group.id
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(15.dp))
                                }
                            }
                        }

                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            Column {
                                if(students.itemCount > 0 ){
                                    Spacer(modifier = Modifier.height(25.dp))

                                    Text(
                                        text = stringResource(id = R.string.students),
                                        color = PgkTheme.colors.primaryText,
                                        style = PgkTheme.typography.heading,
                                        fontFamily = PgkTheme.fontFamily.fontFamily,
                                        modifier = Modifier.padding(start = 20.dp)
                                    )

                                    Spacer(modifier = Modifier.height(15.dp))
                                }
                            }
                        }

                        items(students){ student ->
                            student?.let {
                                StudentCard(
                                    group = groupResult.data!!,
                                    student = student,
                                    onClick = onStudentDetailsScreen
                                )
                            }
                        }

                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun MainMenu(
    openMenu: Boolean,
    visibilityItem: (GroupDetailsMenu) -> Boolean,
    closeMenu: () -> Unit,
    onClick: (GroupDetailsMenu) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.background(PgkTheme.colors.secondaryBackground),
        expanded = openMenu,
        onDismissRequest = closeMenu
    ) {
        GroupDetailsMenu.values().forEach { menu ->

            val color = if(menu == GroupDetailsMenu.DELETE_GROUP)
                PgkTheme.colors.errorColor
            else
                PgkTheme.colors.primaryText

            if (visibilityItem.invoke(menu)){
                DropdownMenuItem(
                    modifier = Modifier.background(PgkTheme.colors.secondaryBackground),
                    onClick = { onClick(menu); closeMenu() }) {
                    Icon(
                        imageVector = menu.icon,
                        contentDescription = null,
                        tint = color
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = stringResource(id = menu.textId),
                        color = color,
                        style = PgkTheme.typography.caption,
                        fontFamily = PgkTheme.fontFamily.fontFamily
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ClassroomTeacherUi(
    classroomTeacher: Teacher,
    onTeacherDetailScreen: (teacherId: Int) -> Unit
) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            backgroundColor = PgkTheme.colors.secondaryBackground,
            shape = PgkTheme.shapes.cornersStyle,
            onClick = { onTeacherDetailScreen(classroomTeacher.id) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if(classroomTeacher.photoUrl != null) {
                    ImageCoil(
                        url = classroomTeacher.photoUrl,
                        modifier = Modifier
                            .width((screenWidthDp / 2).dp)
                            .height((screenHeightDp / 4.3).dp)
                    )
                }else {
                    Image(
                        painter = painterResource(id = R.drawable.profile_photo),
                        contentDescription = null,
                        modifier = Modifier
                            .width((screenWidthDp / 2).dp)
                            .height((screenHeightDp / 4.3).dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${classroomTeacher.lastName} ${classroomTeacher.firstName} " +
                                (classroomTeacher.middleName ?: ""),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(id = R.string.classroomTeacher),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.caption,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun DepartmentAndSpecialityUi(
    department: ru.pgk63.core_model.department.Department,
    speciality: Specialization,
    onClickDepartment: () -> Unit,
    onClickSpecialization: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        shape = PgkTheme.shapes.cornersStyle
    ) {
        Column {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick =onClickSpecialization
            ) {
                Text(
                    text = speciality.name,
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                )
            }

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClickDepartment
            ) {
                Text(
                    text = department.name,
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentCard(
    group: Group,
    student: Student,
    onClick: (studentId: Int) -> Unit
) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Card(
        modifier = Modifier.padding(5.dp),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        shape = PgkTheme.shapes.cornersStyle,
        onClick = { onClick(student.id) }
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(student.photoUrl != null) {
                ImageCoil(
                    url = student.photoUrl,
                    modifier = Modifier
                        .width((screenWidthDp / 2).dp)
                        .height((screenHeightDp / 4.3).dp)
                )
            }else {
                Image(
                    painter = painterResource(id = R.drawable.profile_photo),
                    contentDescription = null,
                    modifier = Modifier
                        .width((screenWidthDp / 2).dp)
                        .height((screenHeightDp / 4.3).dp)
                )
            }
            
            Text(
                text = "${student.lastName} ${student.firstName} " +
                        (student.middleName ?: ""),
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(
                    id = if(group.deputyHeadma?.id == student.id)
                        R.string.deputyHeadma
                    else if(group.headman?.id == student.id)
                        R.string.headman
                    else
                        R.string.student
                ),
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.caption,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}