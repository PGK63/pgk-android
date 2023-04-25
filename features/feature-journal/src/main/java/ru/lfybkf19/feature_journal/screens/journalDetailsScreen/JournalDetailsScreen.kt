package ru.lfybkf19.feature_journal.screens.journalDetailsScreen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.onEach
import ru.lfybkf19.feature_journal.screens.journalDetailsScreen.model.JournalDetailsBottomDrawerType
import ru.lfybkf19.feature_journal.screens.journalDetailsScreen.model.JournalDetailsMenu
import ru.lfybkf19.feature_journal.screens.journalDetailsScreen.viewModel.JournalDetailsViewModel
import ru.lfybkf19.feature_journal.view.JournalTableUi
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.extension.parseToBaseDateFormat
import ru.pgk63.core_common.extension.parseToNetworkFormat
import ru.pgk63.core_common.extension.toDate
import ru.pgk63.core_model.journal.*
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.EmptyUi
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.dialog.calendar.CalendarDialog
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarSelection
import ru.pgk63.core_ui.view.dialog.rememberSheetState
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import java.util.*

@SuppressLint("FlowOperatorInvokedInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun JournalDetailsRoute(
    viewModel: JournalDetailsViewModel = hiltViewModel(),
    journalId: Int,
    journalSubjectId: Int,
    subjectTitle: String,
    subjectTeacher: String,
    subjectHorse: Int,
    subjectTeacherId: Int,
    course: Int,
    semester: Int,
    group: String,
    groupId: Int,
    onJournalTopicTableScreen: (journalSubjectId: Int, maxSubjectHours: Int, teacherId: Int) -> Unit,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val students = viewModel.responseStudentList.collectAsLazyPagingItems()
    val journalRowList = viewModel.responseJournalRowList.collectAsLazyPagingItems()
    val journalTopics = viewModel.responseJournalTopics.collectAsLazyPagingItems()
    var userRole by remember { mutableStateOf<UserRole?>(null) }
    var userId by remember { mutableStateOf<Int?>(null) }
    var journalResponse by remember { mutableStateOf<Result<Unit?>?>(null) }

    var networkModeData by remember { mutableStateOf(true) }
    var journalExistsDatabase by remember { mutableStateOf(false) }

    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    var journalDetailsBottomDrawerType by remember { mutableStateOf<JournalDetailsBottomDrawerType?>(null) }

    viewModel.userLocal.onEach { user ->
        userRole = user.userRole
        userId = user.userId
    }.launchWhenStarted()

    viewModel.responseJournalResult.onEach {
        journalResponse = it
    }.launchWhenStarted()

    viewModel.responseJournalExistsDatabase.onEach {
        journalExistsDatabase = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.journalExistsDatabase(journalId)
    })

//    LaunchedEffect(journalExistsDatabase, journalRowList.itemCount, students.itemCount){
//        if(journalExistsDatabase) {
//            viewModel.updateLocalJournal(
//                journalId = journalId,
//                semester = semester,
//                course = course,
//                group = group,
//                groupId = groupId,
//                journalSubject = journalRowList.itemSnapshotList.items,
//                studentList = students.itemSnapshotList.items
//            )
//        }
//    }

    LaunchedEffect(networkModeData, block = {

        viewModel.getStudents(
            groupIds = listOf(groupId),
            journalId = journalId,
            network = networkModeData
        )

        viewModel.getJournalRowAll(
            journalSubjectId = journalSubjectId,
            network = networkModeData
        )
    })

    LaunchedEffect(key1 = journalDetailsBottomDrawerType, block = {
        if(journalDetailsBottomDrawerType != null){
            bottomDrawerState.open()
        }else {
            bottomDrawerState.close()
        }
    })

    LaunchedEffect(key1 = bottomDrawerState.isOpen, block = {
        if(!bottomDrawerState.isOpen){
            journalDetailsBottomDrawerType = null
        }
    })

    LaunchedEffect(key1 = journalResponse, block = {
        when(journalResponse) {
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
            }
            is Result.Loading -> Unit
            is Result.Success -> {
                journalRowList.refresh()
                journalDetailsBottomDrawerType = null

                Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show()
                Toast.makeText(context, context.getString(R.string.updated), Toast.LENGTH_SHORT).show()

            }
            null -> Unit
        }
    })

    JournalDetailsScreen(
        scaffoldState = scaffoldState,
        bottomDrawerState = bottomDrawerState,
        networkModeData = networkModeData,
        journalExistsDatabase = journalExistsDatabase,
        journalDetailsBottomDrawerType = journalDetailsBottomDrawerType,
        journalTopics = journalTopics,
        userRole = userRole,
        userId = userId,
        subjectTeacher = subjectTeacher,
        subjectHorse = subjectHorse,
        subjectTitle = subjectTitle,
        journalSubjectId = journalSubjectId,
        subjectTeacherId = subjectTeacherId,
        journalResponse = journalResponse,
        journalRowList = journalRowList,
        students = students,
        onJournalTopicTableScreen = onJournalTopicTableScreen,
        onBackScreen = onBackScreen,
        onStudentDetailsScreen = onStudentDetailsScreen,
        onAddColumn = {
            viewModel.createColumn(it)
        },
        onUpdateColumn = { columnId, evaluation ->
            viewModel.updateEvaluation(columnId, evaluation)
        },
        onDeleteColumn = {
            viewModel.deleteColumn(it)
        },
        onJournalDetailsBottomDrawerTypeChange = {
            journalDetailsBottomDrawerType = it
        },
        saveLocalJournal = {
//            viewModel.saveLocalJournal(
//                journalId = journalId,
//                semester = semester,
//                course = course,
//                group = group,
//                groupId = groupId,
//                journalSubject = journalRowList.itemSnapshotList.items,
//                studentList = students.itemSnapshotList.items
//            )
        },
        onNetworkDataChange = {
            networkModeData = it
        },
        getJournalTopics = {
            if(journalTopics.itemCount == 0)
                viewModel.getTopics(journalSubjectId)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun JournalDetailsScreen(
    scaffoldState: ScaffoldState,
    bottomDrawerState: BottomDrawerState,
    networkModeData: Boolean,
    journalExistsDatabase: Boolean,
    journalDetailsBottomDrawerType: JournalDetailsBottomDrawerType?,
    userRole: UserRole?,
    userId: Int?,
    subjectTitle: String,
    subjectTeacher: String,
    journalSubjectId: Int,
    subjectHorse: Int,
    subjectTeacherId: Int,
    journalTopics: LazyPagingItems<JournalTopic>,
    journalResponse: Result<Unit?>?,
    journalRowList: LazyPagingItems<JournalRow>,
    students: LazyPagingItems<Student>,
    onJournalTopicTableScreen: (journalSubjectId: Int, maxSubjectHours: Int, teacherId: Int) -> Unit,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onAddColumn: (body: CreateJournalColumnBody) -> Unit,
    onUpdateColumn: (columnId: Int, evaluation: JournalEvaluation) -> Unit,
    onDeleteColumn: (columnId: Int) -> Unit,
    onJournalDetailsBottomDrawerTypeChange: (JournalDetailsBottomDrawerType?) -> Unit,
    saveLocalJournal: () -> Unit,
    onNetworkDataChange: (Boolean) -> Unit,
    getJournalTopics: () -> Unit
) {
    var showMainMenu by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = subjectTitle,
                onBackClick = onBackScreen,
                modifier = Modifier.clickable {
                    onJournalDetailsBottomDrawerTypeChange(JournalDetailsBottomDrawerType.JournalSubjectDetails)
                },
                actions = {

                    AnimatedVisibility(visible = journalResponse is Result.Loading ||
                            journalRowList.loadState.append is LoadState.Loading
                    ) {
                        CircularProgressIndicator(color = PgkTheme.colors.tintColor)
                    }

                    Box {
                        IconButton(onClick = {
                            showMainMenu = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = PgkTheme.colors.tintColor
                            )
                        }

                        MainMenu(
                            show = showMainMenu,
                            downloadSubjectVisible = false, //!journalExistsDatabase
                            networkModeVisible = false, //journalExistsDatabase
                            enabledNetworkMode = networkModeData,
                            onDismissRequest = { showMainMenu = false }
                        ) { menu ->
                            when (menu) {
                                JournalDetailsMenu.DOWNLOAD -> saveLocalJournal()
                                JournalDetailsMenu.NETWORK_MODE -> {
                                    onNetworkDataChange(!networkModeData)
                                }
                                JournalDetailsMenu.INFO -> {
                                    onJournalDetailsBottomDrawerTypeChange(
                                        JournalDetailsBottomDrawerType.JournalSubjectDetails
                                    )
                                }
                            }
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            if (
                journalRowList.itemCount <= 0 && journalRowList.loadState.refresh !is LoadState.Loading
            ){
                EmptyUi()
            }else if(journalRowList.loadState.refresh is LoadState.Error && networkModeData) {
                val error = (journalRowList.loadState.refresh as LoadState.Error).error
                ErrorUi(error.message)
            }else {
                BottomDrawer(
                    drawerState = bottomDrawerState,
                    drawerBackgroundColor = PgkTheme.colors.secondaryBackground,
                    drawerShape = PgkTheme.shapes.cornersStyle,
                    gesturesEnabled = bottomDrawerState.isOpen,
                    drawerContent = {
                        BottomDrawerContent(
                            journalSubjectId = journalSubjectId,
                            subjectTitle = subjectTitle,
                            subjectTeacher = subjectTeacher,
                            subjectHorse = subjectHorse,
                            journalTeacherId = subjectTeacherId,
                            journalTopics = journalTopics,
                            journalDetailsBottomDrawerType = journalDetailsBottomDrawerType,
                            onJournalTopicTableScreen = onJournalTopicTableScreen,
                            onAddColumn = onAddColumn,
                            onUpdateColumn = onUpdateColumn,
                            onDeleteColumn = onDeleteColumn,
                            getJournalTopics = getJournalTopics
                        )
                    }
                ){
                    if(journalRowList.itemCount > 0){
                        JournalSubjectsUi(
                            journalRowList = journalRowList,
                            userRole = userRole,
                            userId = userId,
                            subjectTeacherId = subjectTeacherId,
                            paddingValues = paddingValues,
                            students = students,
                            onClickStudent = { onStudentDetailsScreen(it.id) }
                        ) { evaluation, columnId, rowId, student, date ->

                            if(userRole == UserRole.ADMIN
                                || (userRole == UserRole.TEACHER && subjectTeacherId == userId)
                            ){
                                onJournalDetailsBottomDrawerTypeChange(
                                    JournalDetailsBottomDrawerType.JournalColumn(
                                        columnId = columnId,
                                        rowId = rowId,
                                        student = student,
                                        date = date,
                                        evaluation = evaluation
                                    )
                                )
                            }
                        }
                    }else {
                        LoadingUi()
                    }
                }
            }
        }
    )
}

@Composable
private fun MainMenu(
    show: Boolean,
    downloadSubjectVisible: Boolean = false,
    networkModeVisible: Boolean = false,
    enabledNetworkMode: Boolean = false,
    onDismissRequest: () -> Unit,
    onClick: (JournalDetailsMenu) -> Unit
) {
    DropdownMenu(
        expanded = show,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.background(PgkTheme.colors.secondaryBackground)
    ) {
        JournalDetailsMenu.values().forEach { menu ->
            if(menu == JournalDetailsMenu.DOWNLOAD){
                if(downloadSubjectVisible){
                    MainMenuItem(menu = menu) { onClick(menu); onDismissRequest() }
                }
            }else if(menu == JournalDetailsMenu.NETWORK_MODE){
                if(networkModeVisible){
                    MainMenuItem(
                        menu = menu,
                        color = if(enabledNetworkMode)
                            PgkTheme.colors.tintColor
                        else
                            PgkTheme.colors.primaryText
                    ) { onClick(menu); onDismissRequest() }
                }
            }else {
                MainMenuItem(menu = menu) { onClick(menu); onDismissRequest() }
            }
        }
    }
}

@Composable
private fun MainMenuItem(
    menu: JournalDetailsMenu,
    color: Color = PgkTheme.colors.primaryText,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        modifier = Modifier.background(PgkTheme.colors.secondaryBackground),
        onClick = onClick
    ) {
        Icon(
            imageVector = menu.icon,
            contentDescription = null,
            tint = color
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = stringResource(id = menu.nameId),
            color = color,
            style = PgkTheme.typography.caption,
            fontFamily = PgkTheme.fontFamily.fontFamily
        )
    }
}

@Composable
private fun BottomDrawerContent(
    journalDetailsBottomDrawerType: JournalDetailsBottomDrawerType?,
    journalSubjectId: Int,
    subjectTitle: String,
    subjectTeacher: String,
    journalTeacherId: Int,
    subjectHorse: Int,
    journalTopics: LazyPagingItems<JournalTopic>,
    onJournalTopicTableScreen: (journalSubjectId: Int, maxSubjectHours: Int, teacherId: Int) -> Unit,
    onAddColumn: (body: CreateJournalColumnBody) -> Unit,
    onUpdateColumn: (columnId: Int, evaluation: JournalEvaluation) -> Unit,
    onDeleteColumn: (columnId: Int) -> Unit,
    getJournalTopics: () -> Unit
) {
    when(journalDetailsBottomDrawerType) {
        JournalDetailsBottomDrawerType.JournalSubjectDetails -> JournalSubjectDetails(
            subjectTitle = subjectTitle,
            journalSubjectId = journalSubjectId,
            subjectTeacher = subjectTeacher,
            journalTeacherId = journalTeacherId,
            subjectHorse = subjectHorse,
            journalTopics = journalTopics,
            onJournalTopicTableScreen = onJournalTopicTableScreen,
            getJournalTopics = getJournalTopics
        )
        is JournalDetailsBottomDrawerType.JournalColumn -> {
            JournalColumn(
                columnId = journalDetailsBottomDrawerType.columnId,
                rowId = journalDetailsBottomDrawerType.rowId,
                student = journalDetailsBottomDrawerType.student,
                date = journalDetailsBottomDrawerType.date,
                evaluation = journalDetailsBottomDrawerType.evaluation,
                onAddColumn = onAddColumn,
                onUpdateColumn = onUpdateColumn,
                onDeleteColumn = onDeleteColumn,
                journalSubjectId = journalSubjectId
            )
        }
        else -> EmptyUi()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun JournalColumn(
    journalSubjectId: Int,
    columnId: Int?,
    rowId: Int?,
    student: Student,
    date: Date?,
    evaluation: JournalEvaluation?,
    onAddColumn: (body: CreateJournalColumnBody) -> Unit,
    onUpdateColumn: (columnId: Int, evaluation: JournalEvaluation) -> Unit,
    onDeleteColumn: (columnId: Int) -> Unit
) {
    val context = LocalContext.current
    val calendarState = rememberSheetState()

    var selectEvaluation by remember { mutableStateOf(evaluation) }
    var selectDate by remember { mutableStateOf(date) }

    val dateText = if(selectDate != null)
        "(${selectDate!!.parseToBaseDateFormat()})"
    else
        ""

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date {
            selectDate = it.toDate()
        }
    )

    LazyColumn {
        item {
            Text(
                text = "${if(evaluation != null && columnId != null)
                    stringResource(id = R.string.edit_journal_column)
                else
                    stringResource(id = R.string.create_journal_column)}\n" +
                        "${student.fio()}\n$dateText",
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.heading,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(15.dp))

            LazyRow {
                item {
                    JournalEvaluation.values().forEach { item ->
                        Card(
                            modifier = Modifier.padding(5.dp),
                            shape = PgkTheme.shapes.cornersStyle,
                            backgroundColor = PgkTheme.colors.primaryBackground,
                            onClick = { selectEvaluation = item },
                            border = if(selectEvaluation == item)
                                BorderStroke(1.dp, PgkTheme.colors.tintColor)
                            else
                                null
                        ) {
                            Text(
                                text = item.text,
                                color = PgkTheme.colors.primaryText,
                                style = PgkTheme.typography.body,
                                fontFamily = PgkTheme.fontFamily.fontFamily,
                                modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            if (date == null) {
                TextButton(onClick = {
                    calendarState.show()
                }){
                    Text(
                        text = stringResource(id = R.string.select_date),
                        color = PgkTheme.colors.tintColor,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            TextButton(onClick = {
                if(columnId == null && selectEvaluation != null && selectDate != null){
                    onAddColumn(
                        CreateJournalColumnBody(
                            journalSubjectId = journalSubjectId,
                            journalSubjectRowId = rowId,
                            evaluation = selectEvaluation!!,
                            studentId = student.id,
                            date = selectDate!!.parseToNetworkFormat()
                        )
                    )
                }else if(columnId != null && selectEvaluation != null){
                    onUpdateColumn(columnId, selectEvaluation!!)
                }else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Text(
                    text = if(evaluation != null && columnId != null)
                        stringResource(id = R.string.edit)
                    else
                        stringResource(id = R.string.add),
                    color = PgkTheme.colors.tintColor,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            if (evaluation != null && columnId != null) {
                TextButton(onClick = {
                    onDeleteColumn(columnId)
                }){
                    Text(
                        text = stringResource(id = R.string.delete),
                        color = PgkTheme.colors.tintColor,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun JournalSubjectDetails(
    subjectTitle: String,
    subjectTeacher: String,
    journalSubjectId: Int,
    journalTeacherId: Int,
    subjectHorse: Int,
    journalTopics: LazyPagingItems<JournalTopic>,
    onJournalTopicTableScreen: (journalSubjectId: Int, maxSubjectHours: Int, teacherId: Int) -> Unit,
    getJournalTopics: () -> Unit
) {
    LaunchedEffect(key1 = Unit, block = {
        getJournalTopics()
    })

    LazyColumn {
        item {
            Text(
                text = "$subjectTitle\n($subjectTeacher)",
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.heading,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onJournalTopicTableScreen(
                            journalSubjectId, subjectHorse, journalTeacherId
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.topics),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.heading,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(5.dp)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = PgkTheme.colors.tintColor
                )
            }
        }

        items(journalTopics){ topic ->
            topic?.let {
                JournalTopicItem(
                    topic = it,
                    maxSubjectHours = subjectHorse
                )
            }
        }
    }
}

@Composable
private fun JournalTopicItem(
    topic: JournalTopic,
    maxSubjectHours: Int
) {
    Divider(color = PgkTheme.colors.primaryBackground)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = topic.date.parseToBaseDateFormat(),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(5.dp)
        )

        Text(
            text = "${topic.hours}/$maxSubjectHours",
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(5.dp)
        )
    }

    Text(
        text = topic.title,
        color = PgkTheme.colors.primaryText,
        style = PgkTheme.typography.body,
        fontFamily = PgkTheme.fontFamily.fontFamily,
        modifier = Modifier.padding(5.dp),
        textAlign = TextAlign.Center
    )

    if(topic.homeWork != null){
        Text(
            text = topic.homeWork.toString(),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Center
        )
    }

    Divider(color = PgkTheme.colors.primaryBackground)
}

@Composable
private fun JournalSubjectsUi(
    journalRowList: LazyPagingItems<JournalRow>,
    userRole: UserRole?,
    userId: Int?,
    subjectTeacherId: Int?,
    paddingValues: PaddingValues,
    students: LazyPagingItems<Student>,
    onClickStudent: (Student) -> Unit,
    onClickEvaluation: (
        JournalEvaluation?,
        columnId: Int?,
        rowId: Int?,
        student: Student,
        date: Date?
    ) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        JournalTableUi(
            rows = journalRowList.itemSnapshotList,
            addColumnButtonVisibility = userRole == UserRole.ADMIN
                    || (userRole == UserRole.TEACHER && subjectTeacherId == userId),
            students = students,
            onClickStudent = onClickStudent,
            onClickEvaluation = onClickEvaluation
        )
    }
}
