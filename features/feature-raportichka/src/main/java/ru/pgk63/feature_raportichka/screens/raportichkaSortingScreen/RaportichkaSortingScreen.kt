package ru.pgk63.feature_raportichka.screens.raportichkaSortingScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.compose.rememberMutableStateListOf
import ru.pgk63.core_common.extension.parseToBaseDateFormat
import ru.pgk63.core_common.extension.toDate
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.dialog.DialogSheetState
import ru.pgk63.core_ui.view.dialog.calendar.CalendarDialog
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarSelection
import ru.pgk63.core_ui.view.dialog.rememberSheetState
import ru.pgk63.feature_raportichka.screens.raportichkaSortingScreen.viewModel.RaportichkaSortingViewModel
import ru.pgk63.core_ui.view.SortingItem
import java.time.LocalDate

@Composable
internal fun RaportichkaSortingRoute(
    viewModel: RaportichkaSortingViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onRaportichkaScreen: (
        studentId: List<Int>?,
        groupsId: List<Int>?,
        subjectsId: List<Int>?,
        teacherId: List<Int>?,
        startDate: String?,
        endDate: String?,
        onlyDate: String?
    ) -> Unit
) {
    val studentsIdSelected = rememberMutableStateListOf<Int>()
    val groupsIdSelected = rememberMutableStateListOf<Int>()
    val subjectsIdSelected = rememberMutableStateListOf<Int>()
    val teachersIdSelected = rememberMutableStateListOf<Int>()

    var studentSearchText by remember { mutableStateOf("") }
    var groupSearchText by remember { mutableStateOf("") }
    var subjectSearchText by remember { mutableStateOf("") }
    var teacherSearchText by remember { mutableStateOf("") }

    val students = viewModel.responseStudent.collectAsLazyPagingItems()
    val groups = viewModel.responseGroup.collectAsLazyPagingItems()
    val subjects = viewModel.responseSubject.collectAsLazyPagingItems()
    val teachers = viewModel.responseTeacher.collectAsLazyPagingItems()

    val calendarState = rememberSheetState()
    var onlyDate by remember { mutableStateOf<LocalDate?>(null) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    val dateText = if(onlyDate != null)
        onlyDate!!.toDate().parseToBaseDateFormat()
    else if(startDate != null && endDate != null)
        "${startDate!!.toDate().parseToBaseDateFormat()} - ${endDate!!.toDate().parseToBaseDateFormat()}"
    else 
        stringResource(id = R.string.select_date)

    LaunchedEffect(studentSearchText,groupsIdSelected, block = {
        viewModel.getStudents(
            search = studentSearchText.ifEmpty { null },
            groupIds = groupsIdSelected.ifEmpty { null }
        )
    })

    LaunchedEffect(key1 = groupSearchText, block = {
        viewModel.getGroups(search = groupSearchText.ifEmpty { null })
    })

    LaunchedEffect(subjectSearchText,teachersIdSelected, block = {
        viewModel.getSubjects(
            search = subjectSearchText.ifEmpty { null },
            teacherIds = teachersIdSelected.ifEmpty { null }
        )
    })

    LaunchedEffect(key1 = teacherSearchText, block = {
        viewModel.getTeacher(search = subjectSearchText.ifEmpty { null })
    })

    RaportichkaSortingScreen(
        dateText = dateText,
        calendarState = calendarState,
        students = students,
        groups = groups,
        subjects = subjects,
        teachers = teachers,
        studentsIdSelected = studentsIdSelected.toList(),
        groupsIdSelected = groupsIdSelected.toList(),
        subjectsIdSelected = subjectsIdSelected.toList(),
        teachersIdSelected = teachersIdSelected.toList(),
        studentSearchText = studentSearchText,
        groupSearchText = groupSearchText,
        subjectSearchText = subjectSearchText,
        teacherSearchText = teacherSearchText,
        onClickStudentItem = { studentId ->
            if(studentId in studentsIdSelected)
                studentsIdSelected.remove(studentId)
            else
                studentsIdSelected.add(studentId)
        },
        onClickGroupItem = { groupId ->
            if(groupId in groupsIdSelected)
                groupsIdSelected.remove(groupId)
            else
                groupsIdSelected.add(groupId)
        },
        onClickSubjectItem = { subjectId ->
            if(subjectId in subjectsIdSelected)
                subjectsIdSelected.remove(subjectId)
            else
                subjectsIdSelected.add(subjectId)
        },
        onClickTeacherItem = { teacherId ->
            if(teacherId in teachersIdSelected)
                teachersIdSelected.remove(teacherId)
            else
                teachersIdSelected.add(teacherId)
        },
        onStudentSearchTextChange = {
            studentSearchText = it       
        },
        onGroupSearchTextChange = {
            groupSearchText = it           
        },
        onSubjectSearchTextChange = {
            subjectSearchText = it          
        },
        onTeacherSearchTextChange = {
            teacherSearchText = it
        },
        onDateChange = { newStartDate, newEndDate ->
            if(newEndDate == null){
                onlyDate = newStartDate
                startDate = null
                endDate = null
            }else {
                startDate = newStartDate
                endDate = newEndDate
                onlyDate = null
            }
        },
        clickDate = {
            calendarState.show()
        },
        onSearchRaportichka = {
            onRaportichkaScreen(
                studentsIdSelected.toList().ifEmpty { null },
                groupsIdSelected.toList().ifEmpty { null },
                subjectsIdSelected.toList().ifEmpty { null },
                teachersIdSelected.toList().ifEmpty { null },
                if(startDate == null) null else startDate.toString(),
                if(endDate == null) null else endDate.toString(),
                if(onlyDate == null) null else onlyDate.toString()
            )
        },
        onBackScreen = onBackScreen
    )
}

@Composable
private fun RaportichkaSortingScreen(
    calendarState: DialogSheetState,
    dateText:String,
    students: LazyPagingItems<Student>,
    groups: LazyPagingItems<Group>,
    subjects: LazyPagingItems<Subject>,
    teachers: LazyPagingItems<Teacher>,
    studentsIdSelected: List<Int>,
    groupsIdSelected: List<Int>,
    subjectsIdSelected: List<Int>,
    teachersIdSelected: List<Int>,
    studentSearchText: String,
    groupSearchText: String,
    subjectSearchText: String,
    teacherSearchText: String,
    onDateChange:(startDate: LocalDate, endDate: LocalDate?) -> Unit,
    onClickStudentItem: (studentId: Int) -> Unit,
    onClickGroupItem: (groupId: Int) -> Unit,
    onClickSubjectItem: (subjectId: Int) -> Unit,
    onClickTeacherItem: (teacherId: Int) -> Unit,
    onStudentSearchTextChange: (String) -> Unit,
    onGroupSearchTextChange: (String) -> Unit,
    onSubjectSearchTextChange: (String) -> Unit,
    onTeacherSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onSearchRaportichka: () -> Unit,
    clickDate: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            Column {
                TopBarBack(
                    title = stringResource(id = R.string.raportichka),
                    scrollBehavior = scrollBehavior,
                    onBackClick = onBackScreen,
                    actions = {
                        TextButton(onClick = onSearchRaportichka) {
                            Text(
                                text = stringResource(id = R.string.search_iskat),
                                color = PgkTheme.colors.tintColor,
                                style = PgkTheme.typography.body,
                                fontFamily = PgkTheme.fontFamily.fontFamily,
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    additionalContent = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(id = R.string.raportichka_sorting_body),
                                color = PgkTheme.colors.primaryText,
                                style = PgkTheme.typography.body,
                                fontFamily = PgkTheme.fontFamily.fontFamily,
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )

                            TextButton(onClick = clickDate) {
                                Text(
                                    text = dateText,
                                    color = PgkTheme.colors.primaryText,
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
        }
    ) { paddingValues ->

        CalendarDialog(
            state = calendarState,
            selection = CalendarSelection.Period { startDate, endDate ->
                onDateChange(startDate, endDate)
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                Spacer(modifier = Modifier.height(30.dp))

                SortingItem(
                    title = stringResource(id = R.string.teachers),
                    content = teachers,
                    searchText = teacherSearchText,
                    selectedItem = { teacher ->
                        teacher.id in teachersIdSelected
                    },
                    onClickItem = { teacher ->
                        onClickTeacherItem(teacher.id)
                    },
                    onSearchTextChange = onTeacherSearchTextChange
                )

                Spacer(modifier = Modifier.height(20.dp))

                SortingItem(
                    title = stringResource(id = R.string.subjects),
                    content = subjects,
                    searchText = subjectSearchText,
                    selectedItem = { subject ->
                        subject.id in subjectsIdSelected
                    },
                    onClickItem = { subject ->
                        onClickSubjectItem(subject.id)
                    },
                    onSearchTextChange = onSubjectSearchTextChange
                )

                Spacer(modifier = Modifier.height(20.dp))

                SortingItem(
                    title = stringResource(id = R.string.groups),
                    content = groups,
                    searchText = groupSearchText,
                    selectedItem = { group ->
                        group.id in groupsIdSelected
                    },
                    onClickItem = { group ->
                        onClickGroupItem(group.id)
                    },
                    onSearchTextChange = onGroupSearchTextChange
                )

                Spacer(modifier = Modifier.height(20.dp))

                SortingItem(
                    title = stringResource(id = R.string.students),
                    content = students,
                    searchText = studentSearchText,
                    selectedItem = { student ->
                        student.id in studentsIdSelected
                    },
                    onClickItem = { student ->
                        onClickStudentItem(student.id)
                    },
                    onSearchTextChange = onStudentSearchTextChange
                )
            }

            item { Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding())) }
        }
    }
}