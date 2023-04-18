package ru.lfybkf19.feature_journal.screens.createJournalScreen

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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.onEach
import ru.lfybkf19.feature_journal.screens.createJournalScreen.viewModel.CreateJournalViewModel
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.validation.numberValidation
import ru.pgk63.core_model.journal.Journal
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.NextButton
import ru.pgk63.core_ui.view.TextFieldBase
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun CreateJournalRoute(
    viewModel: CreateJournalViewModel = hiltViewModel(),
    groupId: Int,
    onBackScreen: () -> Unit,
    onJournalDetailsScreen: (
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
    ) -> Unit,
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    var createJournalResult by remember { mutableStateOf<Result<Journal?>?>(null) }

    viewModel.responseCreateJournalResult.onEach {
        createJournalResult = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = createJournalResult, block = {
        when(createJournalResult) {
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
                viewModel.responseCreateJournalResultToNull()
            }
            is Result.Loading -> Unit
            is Result.Success -> {
                val journal = createJournalResult!!.data!!
                viewModel.responseCreateJournalResultToNull()
                onJournalDetailsScreen(
                    journal.id,
                    journal.course,
                    journal.semester,
                    journal.group.toString(),
                    journal.group.id
                )
            }
            null -> Unit
        }
    })

    CreateJournalScreen(
        scaffoldState = scaffoldState,
        onBackScreen = onBackScreen,
        createJournal = { course, semester ->
            viewModel.createJournal(
                ru.pgk63.core_model.journal.CreateJournalBody(
                    course = course.toInt(),
                    semester = semester.toInt(),
                    groupId = groupId
                )
            )
        }
    )
}

@Composable
private fun CreateJournalScreen(
    scaffoldState: ScaffoldState,
    onBackScreen: () -> Unit,
    createJournal: (course: String, semester: String) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.journal_create),
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
                    CreateJournalUi(
                        createJournal = createJournal
                    )
                }
            }
        }
    )
}

@Composable
private fun CreateJournalUi(
    createJournal: (course: String, semester: String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var course by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }

    val courseValidation = numberValidation(course)
    val semesterValidation = numberValidation(semester)

    val createJournalTextButtonVisible = courseValidation.first && semesterValidation.first

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        TextFieldBase(
            text = course,
            onTextChanged = { course = it },
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.CenterHorizontally),
            errorText = if (courseValidation.second != null)
                stringResource(id = courseValidation.second!!) else null,
            hasError = !courseValidation.first,
            label = stringResource(id = R.string.course),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

        TextFieldBase(
            text = semester,
            onTextChanged = { semester = it },
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.CenterHorizontally),
            errorText = if (semesterValidation.second != null)
                stringResource(id = semesterValidation.second!!) else null,
            hasError = !semesterValidation.first,
            label = stringResource(id = R.string.semester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.clearFocus()
            })
        )

        AnimatedVisibility(visible = createJournalTextButtonVisible) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                NextButton(
                    text = stringResource(id = R.string.add),
                    onClick = {
                        if(createJournalTextButtonVisible) {
                            createJournal(course,semester)
                        }
                    }
                )
            }
        }
    }
}