package ru.pgk63.feature_raportichka.screens.raportichkaAddRow

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.api.raportichka.model.RaportichkaAddRowBody
import ru.pgk63.core_common.api.raportichka.model.RaportichkaUpdateRowBody
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.compose.rememberMutableStateListOf
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_model.raportichka.RaportichkaCause
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TextFieldBase
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_raportichka.screens.raportichkaAddRow.viewModel.RaportichkaAddRowViewModel
import ru.pgk63.core_ui.view.SortingItem

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun RaportichkaAddRowRoute(
    viewModel: RaportichkaAddRowViewModel = hiltViewModel(),
    raportichkaId: Int,
    groupId: Int,
    raportichkaRowId: Int?,
    updateTeacherId: Int?,
    updateNumberLesson:String?,
    updateCountHours:String?,
    updateSubjectId:Int?,
    updateStudentId:Int?,
    onBackScreen: () -> Unit
) {
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()

    var numberLesson by remember { mutableStateOf(updateNumberLesson ?: "") }
    var countHours by remember { mutableStateOf(updateCountHours ?: "2") }

    var studentIdSelected by remember { mutableStateOf(updateStudentId) }
    val studentsIdSelected = rememberMutableStateListOf<Int>()
    var subjectIdSelected by remember { mutableStateOf(updateSubjectId) }
    var teacherIdSelected by remember { mutableStateOf<Int?>(null) }
    var causeSelected by remember { mutableStateOf(RaportichkaCause.STATEMENTS) }

    var studentSearchText by remember { mutableStateOf("") }
    var subjectSearchText by remember { mutableStateOf("") }
    var teacherSearchText by remember { mutableStateOf("") }

    val students = viewModel.responseStudent.collectAsLazyPagingItems()
    val subjects = viewModel.responseSubject.collectAsLazyPagingItems()
    val teachers = viewModel.responseTeacher.collectAsLazyPagingItems()

    var user by remember { mutableStateOf(UserLocalDatabase()) }

    val textTobBar = if(raportichkaRowId != null)
        stringResource(id = R.string.update)
    else
        stringResource(id = R.string.add)

    val textTobBarVisible = if(raportichkaRowId != null && user.userRole == UserRole.TEACHER)
            studentIdSelected != null && subjectIdSelected != null &&
                numberLesson.isNotEmpty() && countHours.isNotEmpty()
        else
            studentIdSelected != null && subjectIdSelected != null &&
                teacherIdSelected != null && numberLesson.isNotEmpty() && countHours.isNotEmpty()

    val teachersListVisible = if(raportichkaRowId != null){
        user.userRole != null && user.userRole != UserRole.TEACHER
    }else true

    viewModel.responseRaportichkaAddRow.onEach { response ->
        when(response){
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
                viewModel.responseRaportichkaAddRowToNull()
            }
            is Result.Loading -> Unit
            is Result.Success -> onBackScreen()
            null -> Unit
        }
    }.launchWhenStarted()

    viewModel.user.onEach { user = it }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        if(updateTeacherId != null)
            teacherIdSelected = updateTeacherId
    })

    LaunchedEffect(studentSearchText, block = {
        viewModel.getStudents(
            search = studentSearchText.ifEmpty { null },
            groupIds = listOf(groupId)
        )
    })

    LaunchedEffect(subjectSearchText,teacherIdSelected, block = {

        val teacherIds:List<Int> = if(teacherIdSelected != null)
                listOf(teacherIdSelected!!)
            else
                emptyList()

        viewModel.getSubjects(
            search = subjectSearchText.ifEmpty { null },
            teacherIds = teacherIds
        )
    })

    LaunchedEffect(key1 = teacherSearchText, block = {
        if(raportichkaRowId != null && user.userRole != UserRole.TEACHER){
            viewModel.getTeacher(search = subjectSearchText.ifEmpty { null })
        }else {
            viewModel.getTeacher(search = subjectSearchText.ifEmpty { null })
        }
    })

    CreateRaportichkaScreen(
        scaffoldState = scaffoldState,
        textTobBar = textTobBar,
        textTobBarVisible = textTobBarVisible,
        numberLesson = numberLesson,
        raportichkaRowId = raportichkaRowId,
        countHours = countHours,
        studentsIdSelected = studentsIdSelected,
        onBackScreen = onBackScreen,
        students = students,
        subjects = subjects,
        teachers = teachers,
        teachersListVisible = teachersListVisible,
        causeSelected = causeSelected,
        studentIdSelected = studentIdSelected,
        subjectIdSelected = subjectIdSelected,
        teacherIdSelected = teacherIdSelected,
        studentSearchText = studentSearchText,
        subjectSearchText = subjectSearchText,
        teacherSearchText = teacherSearchText,
        onClickStudentItem = { studentId ->
            studentIdSelected = studentId
        },
        onClickSubjectItem = { subjectId ->
            subjectIdSelected = subjectId
        },
        onClickTeacherItem = { newTeacherId ->
            teacherIdSelected = newTeacherId
        },
        onStudentSearchTextChange = {
            studentSearchText = it
        },
        onSubjectSearchTextChange = {
            subjectSearchText = it
        },
        onTeacherSearchTextChange = {
            teacherSearchText = it
        },
        onTextNumberLessonChange = {
            numberLesson = it
        },
        onTextCountHoursChange = {
            countHours = it
        },
        onCauseSelectedChange = {
            causeSelected = it
        }
    ) {
        if (raportichkaRowId != null && user.userRole != null) {
            if (user.userRole == UserRole.TEACHER) {
                viewModel.updateRow(
                    rowId = raportichkaRowId,
                    body = RaportichkaUpdateRowBody(
                        numberLesson = numberLesson.toInt(),
                        hours = countHours.toInt(),
                        subjectId = subjectIdSelected!!,
                        studentId = studentIdSelected!!,
                        raportichkaId = raportichkaId,
                        cause = causeSelected
                    )
                )
            } else if (user.userRole == UserRole.HEADMAN
                || user.userRole == UserRole.DEPUTY_HEADMAN
                || user.userRole == UserRole.ADMIN
            ) {
                viewModel.updateRow(
                    rowId = raportichkaRowId,
                    body = ru.pgk63.core_model.headman.HeadmanUpdateRaportichkaRowBody(
                        numberLesson = numberLesson.toInt(),
                        hours = countHours.toInt(),
                        subjectId = subjectIdSelected!!,
                        studentId = studentIdSelected!!,
                        teacherId = teacherIdSelected!!,
                        raportichkaId = raportichkaId,
                        cause = causeSelected
                    )
                )
            }
        } else {
            viewModel.raportichkaAddRow(
                raportichkaId = raportichkaId,
                body = RaportichkaAddRowBody(
                    numberLesson = numberLesson.toInt(),
                    hours = countHours.toInt(),
                    subjectId = subjectIdSelected!!,
                    studentId = studentsIdSelected,
                    teacherId = teacherIdSelected!!,
                    cause = causeSelected
                )
            )
        }
    }
}

@Composable
private fun CreateRaportichkaScreen(
    scaffoldState: ScaffoldState,
    textTobBar: String,
    textTobBarVisible: Boolean,
    studentsIdSelected: SnapshotStateList<Int>,
    raportichkaRowId: Int?,
    numberLesson: String,
    countHours: String,
    causeSelected: RaportichkaCause,
    onCauseSelectedChange: (RaportichkaCause) -> Unit,
    onTextNumberLessonChange: (String) -> Unit,
    onTextCountHoursChange: (String) -> Unit,
    students: LazyPagingItems<Student>,
    subjects: LazyPagingItems<Subject>,
    teachers: LazyPagingItems<Teacher>,
    teachersListVisible: Boolean,
    studentIdSelected: Int?,
    subjectIdSelected: Int?,
    teacherIdSelected: Int?,
    studentSearchText: String,
    subjectSearchText: String,
    teacherSearchText: String,
    onClickStudentItem: (studentId: Int) -> Unit,
    onClickSubjectItem: (subjectId: Int) -> Unit,
    onClickTeacherItem: (teacherId: Int) -> Unit,
    onStudentSearchTextChange: (String) -> Unit,
    onSubjectSearchTextChange: (String) -> Unit,
    onTeacherSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onRaportichkaAddRow: () -> Unit,
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            Column {
                TopBarBack(
                    title = stringResource(id = R.string.raportichka),
                    scrollBehavior = scrollBehavior,
                    onBackClick = onBackScreen,
                    actions = {
                        AnimatedVisibility(
                            visible = textTobBarVisible
                        ) {
                            TextButton(onClick = { if (textTobBarVisible) onRaportichkaAddRow() }) {
                                Text(
                                    text = textTobBar,
                                    color = PgkTheme.colors.tintColor,
                                    style = PgkTheme.typography.body,
                                    fontFamily = PgkTheme.fontFamily.fontFamily,
                                    modifier = Modifier.padding(5.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = { state ->
            SnackbarHost(hostState = state) { data ->
                Snackbar(
                    backgroundColor = PgkTheme.colors.secondaryBackground,
                    contentColor = PgkTheme.colors.primaryText,
                    shape = PgkTheme.shapes.cornersStyle,
                    snackbarData = data
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFieldBase(
                        text = numberLesson,
                        onTextChanged = onTextNumberLessonChange,
                        modifier = Modifier.padding(5.dp),
                        label = stringResource(id = R.string.number_lesson),
                        maxChar = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    TextFieldBase(
                        text = countHours,
                        onTextChanged = onTextCountHoursChange,
                        label = stringResource(id = R.string.count_hours),
                        modifier = Modifier.padding(5.dp),
                        maxChar = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                if(teachersListVisible){
                    SortingItem(
                        title = stringResource(id = R.string.teacher),
                        content = teachers,
                        searchText = teacherSearchText,
                        selectedItem = { teacher ->
                            teacher.id == teacherIdSelected
                        },
                        onClickItem = { teacher ->
                            onClickTeacherItem(teacher.id)
                        },
                        onSearchTextChange = onTeacherSearchTextChange
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                SortingItem(
                    title = stringResource(id = R.string.subject),
                    content = subjects,
                    searchText = subjectSearchText,
                    selectedItem = { subject ->
                        subject.id == subjectIdSelected
                    },
                    onClickItem = { subject ->
                        onClickSubjectItem(subject.id)
                    },
                    onSearchTextChange = onSubjectSearchTextChange
                )

                Spacer(modifier = Modifier.height(20.dp))

                SortingItem(
                    title = stringResource(id = R.string.cause),
                    content = RaportichkaCause.values().toList(),
                    selectedItem = {
                        causeSelected == it
                    },
                    onClickItem = onCauseSelectedChange
                )

                Spacer(modifier = Modifier.height(20.dp))

                SortingItem(
                    title = if(raportichkaRowId != null)
                        stringResource(id = R.string.student)
                    else
                        stringResource(id = R.string.students),
                    content = students,
                    searchText = studentSearchText,
                    selectedItem = { student ->
                        if(raportichkaRowId != null){
                            student.id == studentIdSelected
                        }else {
                            student.id in studentsIdSelected
                        }
                    },
                    onClickItem = { student ->
                        if(raportichkaRowId != null){
                            if(student.id in studentsIdSelected){
                                studentsIdSelected.remove(student.id)
                            }else {
                                studentsIdSelected.add(student.id)
                            }
                        }else {
                            onClickStudentItem(student.id)
                        }
                    },
                    onSearchTextChange = onStudentSearchTextChange
                )
            }
        }
    }
}