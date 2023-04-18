package ru.lfybkf19.feature_journal.screens.journalTopicTableScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.onEach
import ru.lfybkf19.feature_journal.screens.journalTopicTableScreen.model.JournalTopicRowMenu
import ru.lfybkf19.feature_journal.screens.journalTopicTableScreen.model.JournalTopicTableBottomDrawerType
import ru.lfybkf19.feature_journal.screens.journalTopicTableScreen.viewModel.JournalTopicTableViewModel
import ru.lfybkf19.feature_journal.view.JournalTopicTable
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.extension.parseToBaseDateFormat
import ru.pgk63.core_common.extension.toDate
import ru.pgk63.core_common.validation.nameValidation
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.EmptyUi
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.TextFieldBase
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.dialog.calendar.CalendarDialog
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarSelection
import ru.pgk63.core_ui.view.dialog.rememberSheetState
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun JournalTopicTableRoute(
    viewModel: JournalTopicTableViewModel = hiltViewModel(),
    journalSubjectId: Int,
    teacherId: Int,
    maxSubjectHours: Int,
    onBackScreen: () -> Unit
) {
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()
    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    val topics = viewModel.responseJournalTopicList.collectAsLazyPagingItems()
    var createJournalTopicResult by remember { mutableStateOf<Result<Unit?>?>(null) }
    var deleteJournalTopicResult by remember { mutableStateOf<Result<Unit?>?>(null) }
    var user by remember { mutableStateOf(UserLocalDatabase()) }

    var journalTopicTableBottomDrawerType by remember { mutableStateOf<JournalTopicTableBottomDrawerType?>(null) }

    viewModel.responseLocalUser.onEach {
        user = it
    }.launchWhenStarted()

    viewModel.responseCreateJournalTopic.onEach { result ->
        createJournalTopicResult = result
    }.launchWhenStarted()

    viewModel.responseDeleteJournalTopic.onEach { result ->
        deleteJournalTopicResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getJournalTopics(journalSubjectId = journalSubjectId)
    })

    LaunchedEffect(key1 = journalTopicTableBottomDrawerType, block = {
        if(journalTopicTableBottomDrawerType != null){
            bottomDrawerState.open()
        }else {
            bottomDrawerState.close()
        }
    })

    LaunchedEffect(key1 = bottomDrawerState.isOpen, block = {
        if(!bottomDrawerState.isOpen){
            journalTopicTableBottomDrawerType = null
        }
    })

    LaunchedEffect(key1 = createJournalTopicResult, block = {
        when(createJournalTopicResult) {
            is Result.Error -> scaffoldState.snackbarHostState.showSnackbar(
                message = context.getString(R.string.error)
            )
            is Result.Loading -> Unit
            is Result.Success -> {
                journalTopicTableBottomDrawerType = null
                topics.refresh()
            }
            null -> Unit
        }
    })

    LaunchedEffect(key1 = deleteJournalTopicResult, block = {
        when(deleteJournalTopicResult) {
            is Result.Error -> scaffoldState.snackbarHostState.showSnackbar(
                message = context.getString(R.string.error)
            )
            is Result.Loading -> Unit
            is Result.Success -> {
                journalTopicTableBottomDrawerType = null
                topics.refresh()
            }
            null -> Unit
        }
    })

    JournalTopicTableScreen(
        topics = topics,
        user = user,
        teacherId = teacherId,
        maxSubjectHours = maxSubjectHours,
        bottomDrawerState = bottomDrawerState,
        scaffoldState = scaffoldState,
        journalTopicTableBottomDrawerType = journalTopicTableBottomDrawerType,
        onBackScreen = onBackScreen,
        onJournalTopicTableBottomDrawerTypeChange = {
            journalTopicTableBottomDrawerType = it
        },
        createJournalTopic = {
            viewModel.createJournalTopic(journalSubjectId, it)
        },
        deleteTopic = { id ->
            viewModel.deleteJournalTopic(id)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun JournalTopicTableScreen(
    topics: LazyPagingItems<ru.pgk63.core_model.journal.JournalTopic>,
    user: UserLocalDatabase,
    teacherId: Int,
    maxSubjectHours: Int,
    scaffoldState: ScaffoldState,
    bottomDrawerState: BottomDrawerState,
    journalTopicTableBottomDrawerType: JournalTopicTableBottomDrawerType?,
    onJournalTopicTableBottomDrawerTypeChange: (JournalTopicTableBottomDrawerType?) -> Unit,
    onBackScreen: () -> Unit,
    createJournalTopic: (ru.pgk63.core_model.journal.CreateJournalTopicBody) -> Unit,
    deleteTopic: (topicId: Int) -> Unit
) {

    Scaffold(
        backgroundColor = PgkTheme.colors.primaryBackground,
        scaffoldState = scaffoldState,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.topics),
                onBackClick = onBackScreen,
                actions = {
                    if(user.userRole == UserRole.ADMIN
                        || (user.userRole == UserRole.TEACHER && user.userId == teacherId)
                    ){
                        IconButton(onClick = {
                            onJournalTopicTableBottomDrawerTypeChange(JournalTopicTableBottomDrawerType.CreateTopic)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = PgkTheme.colors.tintColor
                            )
                        }
                    }
                }
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
            BottomDrawer(
                drawerState = bottomDrawerState,
                drawerBackgroundColor = PgkTheme.colors.secondaryBackground,
                drawerShape = PgkTheme.shapes.cornersStyle,
                gesturesEnabled = bottomDrawerState.isOpen,
                drawerContent = {
                    BottomDrawerContent(
                        type = journalTopicTableBottomDrawerType,
                        createJournalTopic = createJournalTopic,
                        deleteTopic = deleteTopic
                    )
                }
            ){
                if (
                    topics.itemCount <= 0 && topics.loadState.refresh !is LoadState.Loading
                ){
                    EmptyUi()
                }else if(topics.loadState.refresh is LoadState.Error) {
                    ErrorUi()
                }else if(topics.itemCount > 0){
                    TopicList(
                        topics = topics,
                        maxSubjectHours = maxSubjectHours,
                        paddingValues = paddingValues,
                        onJournalTopicTableBottomDrawerTypeChange = onJournalTopicTableBottomDrawerTypeChange
                    )
                }else {
                    LoadingUi()
                }
            }
        }
    )
}

@Composable
private fun BottomDrawerContent(
    type: JournalTopicTableBottomDrawerType?,
    createJournalTopic: (ru.pgk63.core_model.journal.CreateJournalTopicBody) -> Unit,
    deleteTopic: (topicId: Int) -> Unit
) {
    when(type){
        JournalTopicTableBottomDrawerType.CreateTopic -> CreateTopicUi(
            createJournalTopic = createJournalTopic
        )
        is JournalTopicTableBottomDrawerType.TopicRowMenu -> TopicRowMenu(
            onClick = { menu ->
                when(menu) {
                    JournalTopicRowMenu.DELETE -> deleteTopic(type.topic.id)
                }
            }
        )
        null -> EmptyUi()
    }
}

@Composable
private fun CreateTopicUi(
    createJournalTopic: (ru.pgk63.core_model.journal.CreateJournalTopicBody) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var homeWork by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var date by remember { mutableStateOf<Date?>(null) }

    val titleValidation = nameValidation(title)
    val hoursValidation = nameValidation(title)

    val calendarState = rememberSheetState()

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date {
            date = it.toDate()
        }
    )

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = stringResource(id = R.string.topic),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.heading,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    textAlign = TextAlign.Center
                )

                AnimatedVisibility(
                    visible = title.isNotEmpty() && hours.isNotEmpty() && date != null
                ) {
                    TextButton(onClick = {
                        createJournalTopic(
                            ru.pgk63.core_model.journal.CreateJournalTopicBody(
                                title = title,
                                homeWork = homeWork.ifEmpty { null },
                                hours = hours.toInt()
                            )
                        )
                    }) {
                        Text(
                            text = stringResource(id = R.string.add),
                            color = PgkTheme.colors.tintColor,
                            style = PgkTheme.typography.body,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            TextFieldBase(
                text = title,
                onTextChanged = { title = it },
                label = stringResource(id = R.string.title),
                errorText = if(titleValidation.second != null)
                    stringResource(id = titleValidation.second!!) else null,
                hasError = !titleValidation.first,
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextFieldBase(
                text = homeWork,
                onTextChanged = { homeWork = it },
                label = stringResource(id = R.string.home_work)
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextFieldBase(
                text = hours,
                onTextChanged = { hours = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                errorText = if(hoursValidation.second != null)
                    stringResource(id = hoursValidation.second!!) else null,
                hasError = !hoursValidation.first,
                label = stringResource(id = R.string.hours)
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextButton(onClick = { calendarState.show() }) {
                Text(
                    text = date?.parseToBaseDateFormat() ?: stringResource(id = R.string.select_date),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TopicRowMenu(
    onClick: (JournalTopicRowMenu) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        JournalTopicRowMenu.values().forEach { menu ->
            DropdownMenuItem(onClick = { onClick(menu) }) {
                Icon(
                    imageVector = menu.icon,
                    contentDescription = null,
                    tint = if(menu == JournalTopicRowMenu.DELETE)
                        PgkTheme.colors.errorColor
                    else
                        PgkTheme.colors.primaryText
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = stringResource(id = menu.textId),
                    color = if(menu == JournalTopicRowMenu.DELETE)
                        PgkTheme.colors.errorColor
                    else
                        PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.caption,
                    fontFamily = PgkTheme.fontFamily.fontFamily
                )
            }
        }
    }
}

@Composable
private fun TopicList(
    topics: LazyPagingItems<ru.pgk63.core_model.journal.JournalTopic>,
    maxSubjectHours: Int,
    paddingValues: PaddingValues,
    onJournalTopicTableBottomDrawerTypeChange: (JournalTopicTableBottomDrawerType?) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        JournalTopicTable(
            topics = topics,
            maxSubjectHours = maxSubjectHours
        ) { journalTopic ->
            onJournalTopicTableBottomDrawerTypeChange(
                JournalTopicTableBottomDrawerType.TopicRowMenu(
                    journalTopic
                )
            )
        }
    }
}
