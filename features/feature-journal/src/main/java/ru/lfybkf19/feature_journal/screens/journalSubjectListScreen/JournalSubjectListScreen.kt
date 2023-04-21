package ru.lfybkf19.feature_journal.screens.journalSubjectListScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.onEach
import ru.lfybkf19.feature_journal.screens.journalSubjectListScreen.viewModel.JournalSubjectListViewModel
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_model.journal.JournalSubject
import ru.pgk63.core_navigation.LocalNavController
import ru.pgk63.core_navigation.`typealias`.onJournalDetailsScreen
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.EmptyUi
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun JournalSubjectListRoute(
    viewModel: JournalSubjectListViewModel = hiltViewModel(),
    journalId: Int,
    course: Int,
    semester: Int,
    group: String,
    groupId: Int,
    onCreateJournalSubjectScreen: (journalId: Int) -> Unit,
    onJournalDetailsScreen: onJournalDetailsScreen
) {
    val navController = LocalNavController.current

    val journalSubjectList = viewModel.responseJournalSubjectList.collectAsLazyPagingItems()
    var userRole by remember { mutableStateOf<UserRole?>(null) }

    viewModel.responseUserLocal.onEach { user ->
        userRole = user.userRole
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getJournalSubjectAll(journalId)
    })

    JournalSubjectListScreen(
        navController = navController,
        journalId = journalId,
        course = course,
        semester = semester,
        group = group,
        groupId = groupId,
        userRole = userRole,
        journalSubjectList = journalSubjectList,
        onJournalDetailsScreen = onJournalDetailsScreen,
        onCreateJournalSubjectScreen = onCreateJournalSubjectScreen
    )
}

@Composable
private fun JournalSubjectListScreen(
    navController: NavController,
    journalSubjectList: LazyPagingItems<JournalSubject>,
    journalId: Int,
    course: Int,
    semester: Int,
    group: String,
    groupId: Int,
    userRole: UserRole?,
    onJournalDetailsScreen: onJournalDetailsScreen,
    onCreateJournalSubjectScreen: (journalId: Int) -> Unit,
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.subjects),
                onBackClick = { navController.navigateUp() },
                scrollBehavior = scrollBehavior,
                actions = {
                    if(userRole == UserRole.TEACHER){
                        IconButton(onClick = { onCreateJournalSubjectScreen(journalId) }) {
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
        content = { paddingValues ->

            if (
                journalSubjectList.itemCount <= 0 && journalSubjectList.loadState.refresh !is LoadState.Loading
            ){
                EmptyUi()
            }else if(journalSubjectList.loadState.refresh is LoadState.Error) {
                ErrorUi()
            }else {
                LazyColumn(
                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(journalSubjectList){ item ->
                        item?.let {
                            JournalSubjectItem(
                                journalSubject = it,
                                onClick = {
                                    onJournalDetailsScreen(
                                        journalId,
                                        course,
                                        semester,
                                        group,
                                        groupId,
                                        item.id,
                                        item.subject.subjectTitle,
                                        item.teacher.fio(),
                                        item.hours,
                                        item.teacher.id
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun JournalSubjectItem(
    journalSubject: JournalSubject,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 8.dp,
        shape = PgkTheme.shapes.cornersStyle,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${journalSubject.subject}\n(${journalSubject.teacher})",
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "${stringResource(id = R.string.count_hours)} ${journalSubject.hours}",
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}