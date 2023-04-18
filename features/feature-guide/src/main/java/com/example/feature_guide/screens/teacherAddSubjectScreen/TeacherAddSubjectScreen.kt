package com.example.feature_guide.screens.teacherAddSubjectScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.feature_guide.screens.teacherAddSubjectScreen.viewModel.TeacherAddSubjectViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.SortingItem
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun TeacherAddSubjectRoute(
    viewModel: TeacherAddSubjectViewModel = hiltViewModel(),
    teacherId: Int,
    onBackScreen: () -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    var searchSubjectText by remember { mutableStateOf("") }
    val subjectList = viewModel.responseSubjectList.collectAsLazyPagingItems()

    var teacherAddSubjectResult by remember { mutableStateOf<Result<Unit?>?>(null) }

    viewModel.responseTeacherAddSubjectResult.onEach {
        teacherAddSubjectResult = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = teacherAddSubjectResult, block = {
        when(teacherAddSubjectResult) {
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
            }
            is Result.Loading -> Unit
            is Result.Success -> onBackScreen()
            null -> Unit
        }
    })

    LaunchedEffect(key1 = searchSubjectText, block = {
        viewModel.getSubjectList(
            search = searchSubjectText.ifEmpty { null }
        )
    })

    TeacherAddSubjectScreen(
        scaffoldState = scaffoldState,
        subjectList = subjectList,
        searchSubjectText = searchSubjectText,
        onBackScreen = onBackScreen,
        onSearchSubjectTextChange = { searchSubjectText = it },
        teacherAddSubject = { viewModel.teacherAddSubject(teacherId = teacherId, subjectId = it) }
    )
}

@Composable
private fun TeacherAddSubjectScreen(
    scaffoldState: ScaffoldState,
    subjectList: LazyPagingItems<Subject>,
    searchSubjectText: String,
    onSearchSubjectTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    teacherAddSubject: (subject: Int) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.add_subject),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
            )
        },
        content = { paddingValues ->
            LazyColumn(contentPadding = paddingValues) {
                item {
                    TeacherAddSubjectUi(
                        subjectList = subjectList,
                        searchSubjectText = searchSubjectText,
                        onSearchSubjectTextChange = onSearchSubjectTextChange,
                        teacherAddSubject = teacherAddSubject
                    )
                }
            }
        }
    )
}

@Composable
private fun TeacherAddSubjectUi(
    subjectList: LazyPagingItems<Subject>,
    searchSubjectText: String,
    onSearchSubjectTextChange: (String) -> Unit,
    teacherAddSubject: (subject: Int) -> Unit
) {
    var subjectId by remember { mutableStateOf<Int?>(null) }

    Column {
        SortingItem(
            title = stringResource(id = ru.pgk63.core_common.R.string.subject),
            searchText = searchSubjectText,
            content = subjectList,
            onSearchTextChange = onSearchSubjectTextChange,
            onClickItem = {
                subjectId = it.id
            },
            selectedItem = {
                subjectId == it.id
            }
        )

        AnimatedVisibility(visible = subjectId != null) {
            TextButton(
                onClick = {
                    if(subjectId != null) {
                        teacherAddSubject(subjectId!!)
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.add),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            }
        }
    }
}
