package ru.lfybkf19.feature_journal.screens.journalListScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.launch
import ru.lfybkf19.feature_journal.screens.journalListScreen.model.JournalListBottomDrawerType
import ru.lfybkf19.feature_journal.screens.journalListScreen.viewModel.JournalListViewModel
import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_model.journal.Journal
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.compose.rememberMutableStateListOf
import ru.pgk63.core_database.room.database.journal.model.JournalEntityListItem
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.*
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi

@Composable
internal fun JournalListRoute(
    viewModel: JournalListViewModel = hiltViewModel(),
    groupId:Int?,
    onBackScreen: () -> Unit,
    onJournalDetailsScreen: (
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
    ) -> Unit,
) {
    val journals = viewModel.responseJournalList.collectAsLazyPagingItems()
    val groups = viewModel.responseGroupList.collectAsLazyPagingItems()
    val departments = viewModel.responseDepartmentList.collectAsLazyPagingItems()
    val specialties = viewModel.responseSpecializationList.collectAsLazyPagingItems()
    val journalListDownload = viewModel.journalListDownload.collectAsLazyPagingItems()

    val courseSelected = rememberMutableStateListOf<Int>()
    val semestersSelected = rememberMutableStateListOf<Int>()
    val groupsIdSelected = rememberMutableStateListOf<Int>()
    val departmentsIdSelected = rememberMutableStateListOf<Int>()
    val specialtiesIdSelected = rememberMutableStateListOf<Int>()

    var groupSearchText by remember { mutableStateOf("") }
    var departmentSearchText by remember { mutableStateOf("") }
    var specialitySearchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit){

        if(groupId != null){
            groupsIdSelected.add(groupId)
        }

        viewModel.getJournalList(
            course = courseSelected.ifEmpty { null },
            semesters = semestersSelected.ifEmpty { null },
            groupIds = groupsIdSelected.ifEmpty { null },
            departmentIds = departmentsIdSelected.ifEmpty { null },
            specialityIds = specialtiesIdSelected.ifEmpty { null }
        )
    }

    LaunchedEffect(key1 = groupSearchText, block = {
        viewModel.getGroupList(search = groupSearchText.ifEmpty { null })
    })

    LaunchedEffect(key1 = departmentSearchText, block = {
        viewModel.getDepartmentList(search = departmentSearchText.ifEmpty { null })
    })

    LaunchedEffect(key1 = specialitySearchText, block = {
        viewModel.getSpecializationList(search = specialitySearchText.ifEmpty { null })
    })

    JournalListScreen(
        journals = journals,
        journalListDownload = journalListDownload,
        courseSelected = courseSelected,
        semestersSelected = semestersSelected,
        groups = groups,
        departments = departments,
        specialties = specialties,
        groupsIdSelected = groupsIdSelected,
        departmentsIdSelected = departmentsIdSelected,
        specialtiesIdSelected = specialtiesIdSelected,
        onBackScreen = onBackScreen,
        onJournalDetailsScreen = onJournalDetailsScreen,
        groupSearchText = groupSearchText,
        departmentSearchText = departmentSearchText,
        specialitySearchText = specialitySearchText,
        onGroupSearchTextChange = {
          groupSearchText = it
        },
        onDepartmentSearchTextChange = {
            departmentSearchText = it
        },
        onSpecializationSearchTextChange = {
            specialitySearchText = it
        },
        onClickCourseItem = {
            if(it in courseSelected){
                courseSelected.remove(it)
            }else {
                courseSelected.add(it)
            }
        },
        onClickSemesterItem = {
            if(it in semestersSelected){
                semestersSelected.remove(it)
            }else {
                semestersSelected.add(it)
            }
        },
        onClickGroupItem = {
            if(it in groupsIdSelected){
                groupsIdSelected.remove(it)
            }else {
                groupsIdSelected.add(it)
            }
        },
        onClickDepartmentItem = {
            if(it in departmentsIdSelected){
                departmentsIdSelected.remove(it)
            }else {
                departmentsIdSelected.add(it)
            }
        },
        onClickSpecializationItem = {
            if(it in specialtiesIdSelected){
                specialtiesIdSelected.remove(it)
            }else {
                specialtiesIdSelected.add(it)
            }
        },
        onSortingJournal = {
            viewModel.getJournalList(
                course = courseSelected.ifEmpty { null },
                semesters = semestersSelected.ifEmpty { null },
                groupIds = groupsIdSelected.ifEmpty { null },
                departmentIds = departmentsIdSelected.ifEmpty { null },
                specialityIds = specialtiesIdSelected.ifEmpty { null }
            )
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun JournalListScreen(
    journals: LazyPagingItems<Journal>,
    journalListDownload: LazyPagingItems<JournalEntityListItem>,
    groups: LazyPagingItems<Group>,
    departments: LazyPagingItems<Department>,
    specialties: LazyPagingItems<Specialization>,
    courseSelected: List<Int>,
    semestersSelected: List<Int>,
    groupsIdSelected: List<Int>,
    departmentsIdSelected: List<Int>,
    specialtiesIdSelected: List<Int>,
    groupSearchText: String,
    departmentSearchText: String,
    specialitySearchText: String,
    onGroupSearchTextChange: (String) -> Unit,
    onDepartmentSearchTextChange: (String) -> Unit,
    onSpecializationSearchTextChange: (String) -> Unit,
    onClickCourseItem: (course: Int) -> Unit,
    onClickSemesterItem: (semester: Int) -> Unit,
    onClickGroupItem: (groupId: Int) -> Unit,
    onClickDepartmentItem: (departmentId: Int) -> Unit,
    onClickSpecializationItem: (specializationId: Int) -> Unit,
    onSortingJournal: () -> Unit,
    onBackScreen: () -> Unit,
    onJournalDetailsScreen: (
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
    ) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    var journalListBottomDrawerType by remember { mutableStateOf<JournalListBottomDrawerType?>(null) }

    Scaffold(
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.journals),
                onBackClick = onBackScreen,
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            journalListBottomDrawerType = JournalListBottomDrawerType.Sorting
                            bottomDrawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = null,
                            tint = PgkTheme.colors.tintColor
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            if (
                journals.itemCount <= 0 && journals.loadState.refresh !is LoadState.Loading
                    && journalListDownload.itemCount < 0
            ){
                EmptyUi()
            }else if(journals.loadState.refresh is LoadState.Error && journalListDownload.itemCount < 0) {
                ErrorUi()
            }else{
                BottomDrawer(
                    drawerState = bottomDrawerState,
                    drawerBackgroundColor = PgkTheme.colors.secondaryBackground,
                    drawerShape = PgkTheme.shapes.cornersStyle,
                    gesturesEnabled = bottomDrawerState.isOpen,
                    drawerContent = {
                        BottomDrawerContent(
                            type = journalListBottomDrawerType,
                            courseSelected = courseSelected,
                            semestersSelected = semestersSelected,
                            groups = groups,
                            departments = departments,
                            specialties = specialties,
                            groupsIdSelected = groupsIdSelected,
                            departmentsIdSelected = departmentsIdSelected,
                            specialtiesIdSelected = specialtiesIdSelected,
                            groupSearchText = groupSearchText,
                            departmentSearchText = departmentSearchText,
                            specialitySearchText = specialitySearchText,
                            onClickCourseItem = onClickCourseItem,
                            onClickSemesterItem = onClickSemesterItem,
                            onClickGroupItem = onClickGroupItem,
                            onClickDepartmentItem = onClickDepartmentItem,
                            onClickSpecializationItem = onClickSpecializationItem,
                            onGroupSearchTextChange = onGroupSearchTextChange,
                            onDepartmentSearchTextChange = onDepartmentSearchTextChange,
                            onSpecializationSearchTextChange = onSpecializationSearchTextChange,
                            onSortingJournal = {
                                scope.launch {
                                    onSortingJournal()
                                    journalListBottomDrawerType = null
                                    bottomDrawerState.close()
                                }
                            }
                        )
                    }
                ){
                    if(journals.itemCount > 0 || journalListDownload.itemCount > 0){
                        JournalList(
                            journals = journals,
                            journalListDownload = journalListDownload,
                            paddingValues = paddingValues,
                            onJournalDetailsScreen = onJournalDetailsScreen
                        )
                    }else {
                        LoadingUi()
                    }
                }
            }
        }
    )
}

@Composable
private fun BottomDrawerContent(
    type: JournalListBottomDrawerType?,
    groups: LazyPagingItems<Group>,
    departments: LazyPagingItems<Department>,
    specialties: LazyPagingItems<Specialization>,
    courseSelected: List<Int>,
    semestersSelected: List<Int>,
    groupsIdSelected: List<Int>,
    departmentsIdSelected: List<Int>,
    specialtiesIdSelected: List<Int>,
    groupSearchText: String,
    departmentSearchText: String,
    specialitySearchText: String,
    onClickCourseItem: (course: Int) -> Unit,
    onClickSemesterItem: (semester: Int) -> Unit,
    onClickGroupItem: (groupId: Int) -> Unit,
    onClickDepartmentItem: (departmentId: Int) -> Unit,
    onClickSpecializationItem: (specializationId: Int) -> Unit,
    onGroupSearchTextChange: (String) -> Unit,
    onDepartmentSearchTextChange: (String) -> Unit,
    onSpecializationSearchTextChange: (String) -> Unit,
    onSortingJournal: () -> Unit,
) {
    when(type){
        JournalListBottomDrawerType.Sorting -> JournalSorting(
            groups = groups,
            courseSelected = courseSelected,
            semestersSelected = semestersSelected,
            departments = departments,
            specialties = specialties,
            groupsIdSelected = groupsIdSelected,
            departmentsIdSelected = departmentsIdSelected,
            specialtiesIdSelected = specialtiesIdSelected,
            groupSearchText = groupSearchText,
            departmentSearchText = departmentSearchText,
            specialitySearchText = specialitySearchText,
            onClickCourseItem = onClickCourseItem,
            onClickSemesterItem = onClickSemesterItem,
            onClickGroupItem = onClickGroupItem,
            onClickDepartmentItem = onClickDepartmentItem,
            onClickSpecializationItem = onClickSpecializationItem,
            onGroupSearchTextChange = onGroupSearchTextChange,
            onDepartmentSearchTextChange = onDepartmentSearchTextChange,
            onSpecializationSearchTextChange = onSpecializationSearchTextChange,
            onSortingJournal = onSortingJournal
        )
        else -> EmptyUi()
    }
}

@Composable
private fun JournalSorting(
    groups: LazyPagingItems<Group>,
    departments: LazyPagingItems<Department>,
    specialties: LazyPagingItems<Specialization>,
    courseSelected: List<Int>,
    semestersSelected: List<Int>,
    groupsIdSelected: List<Int>,
    departmentsIdSelected: List<Int>,
    specialtiesIdSelected: List<Int>,
    groupSearchText: String,
    departmentSearchText: String,
    specialitySearchText: String,
    onClickCourseItem: (course: Int) -> Unit,
    onClickSemesterItem: (semester: Int) -> Unit,
    onClickGroupItem: (groupId: Int) -> Unit,
    onClickDepartmentItem: (departmentId: Int) -> Unit,
    onClickSpecializationItem: (specializationId: Int) -> Unit,
    onGroupSearchTextChange: (String) -> Unit,
    onDepartmentSearchTextChange: (String) -> Unit,
    onSpecializationSearchTextChange: (String) -> Unit,
    onSortingJournal: () -> Unit,
) {
    LazyColumn {
        item {

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = stringResource(id = R.string.sorting),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.heading,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    textAlign = TextAlign.Center
                )

                TextButton(onClick = onSortingJournal) {
                    Text(
                        text = stringResource(id = R.string.search_iskat),
                        color = PgkTheme.colors.tintColor,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            SortingItem(
                title = stringResource(id = R.string.course),
                content = (1..6).toList(),
                onClickItem = onClickCourseItem,
                selectedItem = { course ->
                    course in courseSelected
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            SortingItem(
                title = stringResource(id = R.string.semester),
                content = (1..2).toList(),
                onClickItem = onClickSemesterItem,
                selectedItem = { semester ->
                    semester in semestersSelected
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            SortingItem(
                title = stringResource(id = R.string.groups),
                searchText = groupSearchText,
                content = groups,
                onSearchTextChange = onGroupSearchTextChange,
                onClickItem = {
                    onClickGroupItem(it.id)
                },
                selectedItem = { group ->
                    group.id in groupsIdSelected
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            SortingItem(
                title = stringResource(id = R.string.departmens),
                searchText = departmentSearchText,
                content = departments,
                onSearchTextChange = onDepartmentSearchTextChange,
                onClickItem = {
                    onClickDepartmentItem(it.id)
                },
                selectedItem = { department ->
                    department.id in departmentsIdSelected
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            SortingItem(
                title = stringResource(id = R.string.specialties),
                searchText = specialitySearchText,
                content = specialties,
                onSearchTextChange = onSpecializationSearchTextChange,
                onClickItem = {
                    onClickSpecializationItem(it.id)
                },
                selectedItem = { specialization ->
                    specialization.id in specialtiesIdSelected
                }
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun JournalList(
    journals: LazyPagingItems<Journal>,
    journalListDownload: LazyPagingItems<JournalEntityListItem>,
    paddingValues: PaddingValues,
    onJournalDetailsScreen: (
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
    ) -> Unit,
) {
   LazyVerticalGrid(
       columns = GridCells.Fixed(2),
       contentPadding = paddingValues
   ){
       item(span = { GridItemSpan(maxCurrentLineSpan) }) {
           if(journalListDownload.itemCount > 0){
               Column {
                   Spacer(modifier = Modifier.height(25.dp))

                   Text(
                       text = stringResource(id = R.string.uploaded_journals),
                       color = PgkTheme.colors.primaryText,
                       style = PgkTheme.typography.heading,
                       fontFamily = PgkTheme.fontFamily.fontFamily,
                       modifier = Modifier.padding(start = 20.dp)
                   )

                   Spacer(modifier = Modifier.height(5.dp))

                   LazyRow {
                       items(journalListDownload) { journal ->
                           if(journal != null){
                               Box {
                                   JournalUi(
                                       modifier = Modifier
                                           .padding(10.dp)
                                           .align(Alignment.Center),
                                       group = journal.group ?: "",
                                       semester = journal.semester.toString(),
                                       course = journal.course.toString(),
                                       onClick = {
                                           onJournalDetailsScreen(
                                               journal.id,
                                               journal.course,
                                               journal.semester,
                                               journal.group ?: "",
                                               journal.groupId ?: 0
                                           )
                                       }
                                   )
                               }
                           }
                       }
                   }

                   if(journals.itemCount > 0){
                       Spacer(modifier = Modifier.height(10.dp))

                       Text(
                           text = stringResource(id = R.string.journals),
                           color = PgkTheme.colors.primaryText,
                           style = PgkTheme.typography.heading,
                           fontFamily = PgkTheme.fontFamily.fontFamily,
                           modifier = Modifier.padding(start = 20.dp)
                       )

                       Spacer(modifier = Modifier.height(5.dp))
                   }
               }
           }
       }

       items(journals){ journal ->
           if(journal != null) {
               Box {
                   JournalUi(
                       modifier = Modifier
                           .padding(10.dp)
                           .align(Alignment.Center),
                       group = journal.group.toString(),
                       semester = journal.semester.toString(),
                       course = journal.course.toString(),
                       onClick = {
                           onJournalDetailsScreen(
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
   }
}