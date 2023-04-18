package com.example.feature_student.screens.studentListScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.feature_student.screens.studentListScreen.viewModel.StudentListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.StudentItem
import ru.pgk63.core_ui.view.TextFieldSearch
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior

@Composable
internal fun StudentListRoute(
    viewModel: StudentListViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit
) {

    val students = viewModel.responseStudent.collectAsLazyPagingItems()

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = searchText, block = {
        viewModel.getStudents(search = searchText.ifEmpty { null })
    })

    StudentListScreen(
        students = students,
        searchText = searchText,
        onBackScreen = onBackScreen,
        onStudentDetailsScreen = onStudentDetailsScreen,
        onSearchTextChange = { searchText = it }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StudentListScreen(
    students: LazyPagingItems<Student>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = rememberToolbarScrollBehavior()

    val searchTextFieldFocusRequester = remember { FocusRequester() }
    var searchTextFieldVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.students),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
                actions = {
                    AnimatedVisibility(visible = !searchTextFieldVisible) {
                        IconButton(onClick = {
                            scope.launch {
                                searchTextFieldVisible = true
                                delay(100)
                                searchTextFieldFocusRequester.requestFocus()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = PgkTheme.colors.primaryText
                            )
                        }

                    }

                    AnimatedVisibility(visible = searchTextFieldVisible) {
                        TextFieldSearch(
                            text = searchText,
                            onTextChanged = onSearchTextChange,
                            modifier = Modifier.focusRequester(searchTextFieldFocusRequester),
                            onClose = {
                                searchTextFieldVisible = false
                                onSearchTextChange("")
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
            columns = StaggeredGridCells.Fixed(2)
        ) {

            items(students){ student ->
                student?.let {
                    StudentItem(
                        fio = student.fio(),
                        group = student.group.toString(),
                        photoUrl = student.photoUrl,
                        onClick = { onStudentDetailsScreen(student.id) }
                    )
                }
            }
        }
    }
}
