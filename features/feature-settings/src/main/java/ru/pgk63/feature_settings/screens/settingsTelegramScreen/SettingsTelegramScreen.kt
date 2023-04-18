package ru.pgk63.feature_settings.screens.settingsTelegramScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.Constants.TELEGRAM_START_BOT_URL
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.extension.openBrowser
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.*
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.feature_settings.screens.settingsTelegramScreen.viewModel.SettingsTelegramViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun SettingsTelegramRoute(
    viewModel: SettingsTelegramViewModel = hiltViewModel(),
    onBackScreen: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    var telegramTokenResult by remember { mutableStateOf<Result<String>>(Result.Loading()) }

    viewModel.responseTelegramToken.onEach { result ->
        telegramTokenResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getTelegramToken()
    })

    SettingsTelegramScreen(
        scaffoldState = scaffoldState,
        telegramTokenResult = telegramTokenResult,
        onBackScreen = onBackScreen
    )
}

@Composable
private fun SettingsTelegramScreen(
    scaffoldState: ScaffoldState,
    telegramTokenResult: Result<String>,
    onBackScreen: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.telegram),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        },
        snackbarHost = { snackbarHostState ->
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    backgroundColor = PgkTheme.colors.secondaryBackground,
                    contentColor = PgkTheme.colors.primaryText,
                    shape = PgkTheme.shapes.cornersStyle,
                    snackbarData = data
                )
            }
        }
    ){ paddingValues ->
        when(telegramTokenResult){
            is Result.Error -> ErrorUi()
            is Result.Loading -> LoadingUi()
            is Result.Success -> SuccessUi(
                contentPadding = paddingValues,
                telegramToken = telegramTokenResult.data!!
            )
        }
    }
}

@Composable
private fun SuccessUi(
    contentPadding: PaddingValues,
    telegramToken: String
) {
    val context = LocalContext.current

    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        item {
            BaseLottieAnimation(
                type = LottieAnimationType.TELEGRAM,
                modifier = Modifier
                    .padding(5.dp)
                    .width((screenWidthDp / 1.5).dp)
                    .height((screenHeightDp / 3).dp)
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.telegram_password_body),
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )

            TextButton(onClick = { context.openBrowser("${TELEGRAM_START_BOT_URL}${telegramToken}") }) {
                Text(
                    text = stringResource(id = R.string.authorization_telegram_bot),
                    color = PgkTheme.colors.tintColor,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}