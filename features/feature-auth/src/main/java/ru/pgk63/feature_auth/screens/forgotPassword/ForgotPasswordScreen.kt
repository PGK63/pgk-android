@file:OptIn(ExperimentalPagerApi::class)

package ru.pgk63.feature_auth.screens.forgotPassword

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.validation.emailValidation
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.*
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_auth.screens.forgotPassword.viewModel.ForgotPasswordViewModel

private const val PAGER_INDEX_MANUAL = 0
private const val PAGER_INDEX_RESTORE_ACCESS_EMAIL = 1
private const val PAGER_INDEX_RESTORE_ACCESS_TELEGRAM = 2

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun ForgotPasswordRoute(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
) {
    val content = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    var repeatSendEmailSeconds by remember { mutableStateOf(0) }

    var responsePasswordResetResult by remember { mutableStateOf<Result<Unit?>?>(null) }
    
    val repeatSendEmailTimer = object: CountDownTimer(20000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            repeatSendEmailSeconds = (millisUntilFinished / 1000).toInt()
        }

        override fun onFinish() {
            repeatSendEmailSeconds = 0
        }
    }

    viewModel.responsePasswordReset.onEach {
        responsePasswordResetResult = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = responsePasswordResetResult, block = {
        when(responsePasswordResetResult) {
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = content.getString(R.string.error)
                )
            }
            is Result.Loading -> Unit
            is Result.Success -> {
                repeatSendEmailTimer.start()

                scaffoldState.snackbarHostState.showSnackbar(
                    message = content.getString(R.string.send_email_success)
                )
            }
            null -> Unit
        }
    })
    
    ForgotPasswordScreen(
        scaffoldState = scaffoldState,
        repeatSendEmailSeconds = repeatSendEmailSeconds,
        onBackScreen = onBackScreen,
        passwordReset = {
            viewModel.passwordReset(it)
        }
    )
}

@Composable
private fun ForgotPasswordScreen(
    scaffoldState: ScaffoldState,
    repeatSendEmailSeconds: Int?,
    onBackScreen: () -> Unit,
    passwordReset: (email: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.forgot_password),
                scrollBehavior = scrollBehavior,
                onBackClick = {
                    if(pagerState.currentPage == PAGER_INDEX_MANUAL){
                        onBackScreen()
                    }else {
                        scope.launch {
                            pagerState.animateScrollToPage(PAGER_INDEX_MANUAL)
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
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            contentPadding = paddingValues,
            userScrollEnabled = true,
            count = 3
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when(it){
                    PAGER_INDEX_MANUAL -> manual()
                    PAGER_INDEX_RESTORE_ACCESS_EMAIL -> restoreAccessEmail(
                        passwordReset = passwordReset,
                        repeatSendEmailSeconds = repeatSendEmailSeconds
                    )
                    PAGER_INDEX_RESTORE_ACCESS_TELEGRAM -> restoreAccessTelegram()
                    else -> item { EmptyUi() }
                }
            }
        }
    }
}

private fun LazyListScope.manual() {
    item {
        val screenHeightDp = LocalConfiguration.current.screenHeightDp
        val screenWidthDp = LocalConfiguration.current.screenWidthDp

        BaseLottieAnimation(
            type = LottieAnimationType.SWIPE_TO_SCREEN,
            modifier = Modifier
                .padding(5.dp)
                .width((screenWidthDp / 1.5).dp)
                .height((screenHeightDp / 3).dp)
        )

        Text(
            text = stringResource(id = R.string.email_or_telegram_question),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.toolbar,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = stringResource(id = R.string.swipe_to_screen),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.heading,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp)
        )
    }
}

private fun LazyListScope.restoreAccessTelegram() {
    item {
        val screenHeightDp = LocalConfiguration.current.screenHeightDp
        val screenWidthDp = LocalConfiguration.current.screenWidthDp

        BaseLottieAnimation(
            type = LottieAnimationType.TELEGRAM,
            modifier = Modifier
                .padding(5.dp)
                .width((screenWidthDp / 1.5).dp)
                .height((screenHeightDp / 2).dp)
        )

        Text(
            text = stringResource(id = R.string.restore_access_telegram_body),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(5.dp)
        )
    }
}

private fun LazyListScope.restoreAccessEmail(
    repeatSendEmailSeconds: Int?,
    passwordReset: (email: String) -> Unit
) {
    item {
        val screenHeightDp = LocalConfiguration.current.screenHeightDp
        val screenWidthDp = LocalConfiguration.current.screenWidthDp

        var email by remember { mutableStateOf("") }
        val emailValidation = emailValidation(email)

        val sendEmailTextButton = if(repeatSendEmailSeconds == null || repeatSendEmailSeconds == 0)
            stringResource(id = R.string.send_email)
        else
            "${stringResource(id = R.string.send_email)} ($repeatSendEmailSeconds)"

        BaseLottieAnimation(
            type = LottieAnimationType.EMAIL,
            modifier = Modifier
                .padding(5.dp)
                .width((screenWidthDp / 1.5).dp)
                .height((screenHeightDp / 2).dp)
        )

        Text(
            text = stringResource(id = R.string.restore_access_email_body),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(5.dp)
        )

        TextFieldBase(
            text = email,
            onTextChanged = { email = it },
            modifier = Modifier.padding(5.dp),
            label = stringResource(id = R.string.email),
            errorText = if (emailValidation.second != null)
                stringResource(id = emailValidation.second!!) else null,
            hasError = !emailValidation.first,
            maxChar = 256,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextButton(onClick = {
            if(emailValidation.first && (repeatSendEmailSeconds == null || repeatSendEmailSeconds == 0)){
                passwordReset(email.trim())
            }
        }) {
            Text(
                text = sendEmailTextButton,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}