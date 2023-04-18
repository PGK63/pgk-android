package ru.pgk63.feature_main.screens.searchScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_model.departmentHead.DepartmentHead
import ru.pgk63.core_common.api.director.model.Director
import ru.pgk63.core_common.api.search.model.SearchType
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_main.screens.searchScreen.viewModel.SearchViewModel
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.view.*
import ru.pgk63.core_ui.view.shimmer.VerticalListItemShimmer

private const val DELAY_SEARCH_REQUEST = 100L

@Composable
internal fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit,
    onDepartmentHeadDetailsScreen: (departmentHeadId: Int) -> Unit,
    onDirectorDetailsScreen: (directorId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    val studentList = viewModel.responseStudentList.collectAsLazyPagingItems()
    val departmentList = viewModel.responseDepartmentList.collectAsLazyPagingItems()
    val departmentHeadList = viewModel.responseDepartmentHeadList.collectAsLazyPagingItems()
    val directorList = viewModel.responseDirectorList.collectAsLazyPagingItems()
    val groupList = viewModel.responseGroupList.collectAsLazyPagingItems()
    val specializationList = viewModel.responseSpecializationList.collectAsLazyPagingItems()
    val subjectList = viewModel.responseSubjectList.collectAsLazyPagingItems()
    val teacherList = viewModel.responseTeacherList.collectAsLazyPagingItems()

    LaunchedEffect(searchText, block = {
        viewModel.getStudentList(search = searchText.ifEmpty { null })
        delay(DELAY_SEARCH_REQUEST)
        viewModel.getTeacherList(search = searchText.ifEmpty { null })
        delay(DELAY_SEARCH_REQUEST)
        viewModel.getDepartmentHeadList(search = searchText.ifEmpty { null })
        delay(DELAY_SEARCH_REQUEST)
        viewModel.getDirectorList(search = searchText.ifEmpty { null })
        delay(DELAY_SEARCH_REQUEST)
        viewModel.getDepartmentList(search = searchText.ifEmpty { null })
        delay(DELAY_SEARCH_REQUEST)
        viewModel.getGroupList(search = searchText.ifEmpty { null })
        delay(DELAY_SEARCH_REQUEST)
        viewModel.getSpecializationList(search = searchText.ifEmpty { null })
        delay(DELAY_SEARCH_REQUEST)
        viewModel.getSubjectList(search = searchText.ifEmpty { null })
    })

    SearchScreen(
        searchText = searchText,
        departmentList = departmentList,
        departmentHeadList = departmentHeadList,
        directorList = directorList,
        groupList = groupList,
        specializationList = specializationList,
        subjectList = subjectList,
        teacherList = teacherList,
        studentList = studentList,
        onBackScreen = onBackScreen,
        onSearchTextChange = { searchText = it },
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun SearchScreen(
    searchText: String,
    departmentList: LazyPagingItems<Department>,
    departmentHeadList: LazyPagingItems<DepartmentHead>,
    directorList: LazyPagingItems<Director>,
    groupList: LazyPagingItems<Group>,
    specializationList: LazyPagingItems<Specialization>,
    subjectList: LazyPagingItems<Subject>,
    teacherList: LazyPagingItems<Teacher>,
    studentList: LazyPagingItems<Student>,
    onSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit,
    onDepartmentHeadDetailsScreen: (departmentHeadId: Int) -> Unit,
    onDirectorDetailsScreen: (directorId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val searchTextFieldFocusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val scrollBehavior = rememberToolbarScrollBehavior()
    val pagerState = rememberPagerState()

    LaunchedEffect(key1 = Unit, block = {
        searchTextFieldFocusRequester.requestFocus()
    })

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = null,
                scrollBehavior = scrollBehavior,
                backgroundColor = PgkTheme.colors.secondaryBackground,
                onBackClick = onBackScreen,
                centralContent = {
                    TextFieldSearch(
                        text = searchText,
                        onTextChanged = onSearchTextChange,
                        closeVisible = searchText.isNotEmpty(),
                        modifier = Modifier.focusRequester(searchTextFieldFocusRequester),
                        onSearch = { focusManager.clearFocus() },
                        onClose = {
                            onSearchTextChange("")
                            focusManager.clearFocus()
                        }
                    )
                },
                additionalContent = {
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        backgroundColor = PgkTheme.colors.secondaryBackground,
                        contentColor = PgkTheme.colors.tintColor
                    ) {
                        SearchType.values().forEach { item ->
                            Tab(
                                selected = pagerState.currentPage == item.ordinal,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(item.ordinal)
                                    }
                                },
                                text = {
                                    Text(
                                        text = stringResource(id = item.nameId),
                                        color = PgkTheme.colors.primaryText,
                                        fontFamily = PgkTheme.fontFamily.fontFamily,
                                        style = PgkTheme.typography.body
                                    )
                                }
                            )
                        }
                    }
                }
            )
        }
    ){ paddingValues ->
        HorizontalPager(
            count = SearchType.values().size,
            state = pagerState
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize()
                ) {
                    when(it) {
                        SearchType.STUDENT.ordinal -> {
                            searchItem(studentList) { student ->
                                StudentItem(
                                    fio = student.fio(),
                                    group = student.group.toString(),
                                    photoUrl = student.photoUrl,
                                    onClick = { onStudentDetailsScreen(student.id) }
                                )
                            }
                        }
                        SearchType.TEACHER.ordinal -> {
                            searchItem(teacherList) { teacher ->
                                GuideItemUi(
                                    name = teacher.fio(),
                                    post = stringResource(id = R.string.teacher),
                                    photoUrl = teacher.photoUrl,
                                    onClick = { onTeacherDetailsScreen(teacher.id) }
                                )
                            }
                        }
                        SearchType.DEPARTMENT_HEAD.ordinal -> {
                            searchItem(departmentHeadList) { departmentHead ->
                                GuideItemUi(
                                    name = departmentHead.fio(),
                                    post = stringResource(id = ru.pgk63.core_common.R.string.department_head),
                                    photoUrl = departmentHead.photoUrl,
                                    onClick = { onDepartmentHeadDetailsScreen(departmentHead.id) }
                                )
                            }
                        }
                        SearchType.DIRECTOR.ordinal -> {
                            searchItem(directorList) { director ->
                                GuideItemUi(
                                    name = director.fio(),
                                    post = stringResource(id = ru.pgk63.core_common.R.string.director),
                                    photoUrl = director.photoUrl,
                                    onClick = { onDirectorDetailsScreen(director.id) }
                                )
                            }
                        }
                        SearchType.DEPARTMENT.ordinal -> {
                            searchItem(departmentList, maxSizeScreen = true) {department ->
                                DepartmentItem(
                                    department = department.name,
                                    onClick = { onDepartmentDetailsScreen(department.id) }
                                )
                            }
                        }
                        SearchType.GROUP.ordinal -> {
                            searchItem(groupList) { group ->
                                GroupItem(
                                    group = group.toString(),
                                    classroomTeacher = group.classroomTeacher.fioAbbreviated(),
                                    onClick = { onGroupDetailsScreen(group.id) }
                                )
                            }
                        }
                        SearchType.SPECIALITY.ordinal -> {
                            searchItem(specializationList, maxSizeScreen = true) { specialization ->
                                SpecializationItem(
                                    specialization = specialization.name,
                                    onClick = { onSpecializationDetailsScreen(specialization.id) }
                                )
                            }
                        }
                        SearchType.SUBJECT.ordinal -> {
                            searchItem(subjectList) { subject ->
                                SubjectItem(
                                    subjectTitle = subject.subjectTitle,
                                    onClick = { onSubjectDetailsScreen(subject.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun <T : Any> LazyGridScope.searchItem(
    items: LazyPagingItems<T>,
    maxSizeScreen: Boolean = false,
    contentItem: @Composable (T) -> Unit
) {
    if (items.loadState.append is LoadState.Loading){
        item {
            VerticalListItemShimmer()
        }
    }

    if (items.loadState.refresh is LoadState.Loading){
        items(20) {
            VerticalListItemShimmer()
        }
    }

    if(maxSizeScreen) {
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Column {
                repeat(items.itemCount) {
                    val item = items[it]

                    if(item != null){
                        contentItem(item)
                    }
                }
            }
        }
    }else {
        items(items) { item ->
            if(item != null) {
                contentItem(item)
            }
        }
    }
}