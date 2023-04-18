package ru.lfybkf19.feature_journal.screens.createJournalSubject

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.onEach
import ru.lfybkf19.feature_journal.screens.createJournalSubject.viewModel.CreateJournalSubjectViewModel
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.validation.numberValidation
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.NextButton
import ru.pgk63.core_ui.view.SortingItem
import ru.pgk63.core_ui.view.TextFieldBase
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun CreateJournalSubjectRoute(
    viewModel: CreateJournalSubjectViewModel = hiltViewModel(),
    journalId: Int,
    onBackScreen: () -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val subjects = viewModel.responseSubjectList.collectAsLazyPagingItems()
    var createJournalSubjectResult by remember { mutableStateOf<Result<Unit?>?>(null) }

    var searchText by remember { mutableStateOf("") }

    viewModel.responseCreateJournalSubjectResult.onEach {
        createJournalSubjectResult = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getSubjectList(search = searchText.ifEmpty { null })
    })

    LaunchedEffect(key1 = createJournalSubjectResult, block = {
        when(createJournalSubjectResult) {
            is Result.Error -> scaffoldState.snackbarHostState.showSnackbar(
                message = context.getString(R.string.error)
            )
            is Result.Loading -> Unit
            is Result.Success -> onBackScreen()
            null -> Unit
        }
    })

    CreateJournalSubjectScreen(
        scaffoldState = scaffoldState,
        subjects = subjects,
        onBackScreen = onBackScreen,
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        createJournalSubject = { body ->
            viewModel.createJournalSubject(
                journalId = journalId,
                body = body
            )
        }
    )
}

@Composable
private fun CreateJournalSubjectScreen(
    scaffoldState: ScaffoldState,
    subjects: LazyPagingItems<ru.pgk63.core_model.subject.Subject>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    createJournalSubject: (ru.pgk63.core_model.journal.CreateJournalSubjectBody) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.journal_subject_create),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
            )
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
        },
        content = { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    CreateJournalSubjectUi(
                        subjects = subjects,
                        searchText = searchText,
                        onSearchTextChange = onSearchTextChange,
                        createJournalSubject = createJournalSubject
                    )
                }
            }
        }
    )
}

@Composable
private fun CreateJournalSubjectUi(
    subjects: LazyPagingItems<ru.pgk63.core_model.subject.Subject>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    createJournalSubject: (ru.pgk63.core_model.journal.CreateJournalSubjectBody) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var hours by remember { mutableStateOf("") }
    var subjectId by remember { mutableStateOf<Int?>(null) }

    val hoursValidation = numberValidation(hours)

    val createJournalSubjectTextButtonVisible = hoursValidation.first && subjectId != null

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        TextFieldBase(
            text = hours,
            onTextChanged = { hours = it },
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.CenterHorizontally),
            errorText = if (hoursValidation.second != null)
                stringResource(id = hoursValidation.second!!) else null,
            hasError = !hoursValidation.first,
            label = stringResource(id = R.string.hours),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.clearFocus()
            })
        )

        Spacer(modifier = Modifier.height(10.dp))

        SortingItem(
            modifier = Modifier.fillMaxWidth().align(Alignment.Start),
            title = stringResource(id = R.string.subject),
            content = subjects,
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onClickItem = {
                subjectId = it.id
            },
            selectedItem = {
                subjectId == it.id
            }
        )

        AnimatedVisibility(visible = createJournalSubjectTextButtonVisible) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                NextButton(
                    text = stringResource(id = R.string.add),
                    onClick = {
                        if(createJournalSubjectTextButtonVisible) {
                            createJournalSubject(
                                ru.pgk63.core_model.journal.CreateJournalSubjectBody(
                                    hours = hours.toInt(),
                                    subjectId = subjectId!!
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}