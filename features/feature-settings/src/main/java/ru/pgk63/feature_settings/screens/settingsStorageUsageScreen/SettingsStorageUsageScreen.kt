package ru.pgk63.feature_settings.screens.settingsStorageUsageScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_settings.screens.settingsStorageUsageScreen.view.PieChart
import ru.pgk63.feature_settings.screens.settingsStorageUsageScreen.viewModel.SettingsStorageUsageViewModel
import ru.pgk63.feature_settings.view.SettingsButton

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun SettingsStorageUsageRoute(
    viewModel: SettingsStorageUsageViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
) {
    var databaseSizeKb by remember { mutableStateOf(0) }
    var historyCount by remember { mutableStateOf(0) }
    var journalCount by remember { mutableStateOf(0) }

    viewModel.databaseSizeKb.onEach { result ->
        databaseSizeKb = result
    }.launchWhenStarted()

    viewModel.historyCount.onEach { count ->
        historyCount = count
    }.launchWhenStarted()

    viewModel.journalCount.onEach { count ->
        journalCount = count
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getDatabaseSizeInfo()
    })

    SettingsStorageUsageScreen(
        databaseSizeKb = databaseSizeKb,
        historyCount = historyCount,
        journalCount = journalCount,
        onBackScreen = onBackScreen,
        clearHistory = { viewModel.clearHistory() },
        clearJournal = { viewModel.clearJournal() }
    )
}

@Composable
private fun SettingsStorageUsageScreen(
    databaseSizeKb: Int,
    historyCount: Int,
    journalCount: Int,
    onBackScreen: () -> Unit,
    clearHistory: () -> Unit,
    clearJournal: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.storage),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                PieChart(
                    indicatorValue = databaseSizeKb,
                )

                Spacer(modifier = Modifier.height(10.dp))

                SettingsButton(
                    title = stringResource(id = R.string.history),
                    body = "${stringResource(id = R.string.history_body)} ($historyCount)",
                    onClick = { clearHistory() },
                    rowContent = {
                        Text(
                            text = "${stringResource(id = R.string.clear)} ($historyCount)",
                            color = PgkTheme.colors.errorColor,
                            style = PgkTheme.typography.body,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                )

                SettingsButton(
                    title = stringResource(id = R.string.journal),
                    onClick = { clearJournal() },
                    rowContent = {
                        Text(
                            text = "${stringResource(id = R.string.clear)} ($journalCount)",
                            color = PgkTheme.colors.errorColor,
                            style = PgkTheme.typography.body,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                )
            }
        }
    }
}