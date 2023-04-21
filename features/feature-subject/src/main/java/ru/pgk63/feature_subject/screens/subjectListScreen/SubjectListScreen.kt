package ru.pgk63.feature_subject.screens.subjectListScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_model.subject.CreateSubjectBody
import ru.pgk63.core_model.subject.CreateSubjectResponse
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.SubjectItem
import ru.pgk63.core_ui.view.TextFieldSearch
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_subject.screens.subjectListScreen.view.CreateSubjectDialog
import ru.pgk63.feature_subject.screens.subjectListScreen.viewModel.SubjectListViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun SubjectListRoute(
    viewModel: SubjectListViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    var searchText by remember { mutableStateOf("") }

    val subjects = viewModel.responseSubject.collectAsLazyPagingItems()
    var createSubjectResult by remember { mutableStateOf<Result<CreateSubjectResponse>?>(null) }
    var user by remember { mutableStateOf(UserLocalDatabase()) }

    LaunchedEffect(key1 = searchText, block = {
        viewModel.getSubjectAll(search = searchText.ifEmpty { null })
    })

    viewModel.responseLocalUser.onEach {
        user = it
    }.launchWhenStarted()

    viewModel.responseCreateSubjectResult.onEach {
        createSubjectResult = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = createSubjectResult, block = {
        when(createSubjectResult){
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
                viewModel.createSubjectResultTNull()
            }
            is Result.Loading -> Unit
            is Result.Success -> {
                val subjectId = createSubjectResult!!.data!!.id
                viewModel.createSubjectResultTNull()
                onSubjectDetailsScreen(subjectId)
            }
            null -> Unit
        }
    })

    SubjectListScreen(
        scaffoldState = scaffoldState,
        subjects = subjects,
        user = user,
        searchText = searchText,
        onBackScreen = onBackScreen,
        onSubjectDetailsScreen = onSubjectDetailsScreen,
        onSearchTextChange = { searchText = it },
        onCreateSubject = {
            viewModel.createSubject(it)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SubjectListScreen(
    scaffoldState: ScaffoldState,
    subjects: LazyPagingItems<Subject>,
    user: UserLocalDatabase,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onCreateSubject: (CreateSubjectBody) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = rememberToolbarScrollBehavior()

    val searchTextFieldFocusRequester = remember { FocusRequester() }
    var searchTextFieldVisible by remember { mutableStateOf(false) }

    var createSubjectDialogShow by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.subjects),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
                actions = {
                    AnimatedVisibility(visible = !searchTextFieldVisible) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

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

                            if(user.userRole == UserRole.TEACHER
                                || user.userRole == UserRole.EDUCATIONAL_SECTOR
                                || user.userRole == UserRole.ADMIN
                            ) {
                                Spacer(modifier = Modifier.height(5.dp))

                                IconButton(onClick = { createSubjectDialogShow = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = PgkTheme.colors.primaryText
                                    )
                                }
                            }
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

        CreateSubjectDialog(
            show = createSubjectDialogShow,
            onDismissRequest = {
                createSubjectDialogShow = false
            },
            onCreateSubjectButtonClick = onCreateSubject
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {

            items(subjects){ subject ->
                subject?.let {
                    SubjectItem(
                        subjectTitle = subject.subjectTitle,
                        onClick = { onSubjectDetailsScreen(subject.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier
                    .height(paddingValues.calculateBottomPadding()))
            }
        }
    }
}