package ru.pgk63.feature_settings.screens.settingsEmailScreen

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.Lifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.api.user.model.UserDetails
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.security.model.SecurityEmailState
import ru.pgk63.core_common.security.model.getSecurityEmailState
import ru.pgk63.core_common.validation.emailValidation
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.*
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.feature_settings.screens.settingsEmailScreen.viewModel.SettingsEmailViewModel

private const val PAGER_INDEX_EMAIL_SECURITY = 0
private const val PAGER_INDEX_EMAIL_VERIFICATION = 1
private const val PAGER_INDEX_EMAIL_INSECURITY = 2
private const val PAGER_INDEX_EMAIL_SIZE = 3

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun SettingsEmailRoute(
    viewModel: SettingsEmailViewModel = hiltViewModel(),
    onBackScreen: () -> Unit
) {
    val content = LocalContext.current

    val scaffoldState = rememberScaffoldState()
    var userResult by remember { mutableStateOf<Result<UserDetails>>(Result.Loading()) }

    var repeatSendEmailSeconds by remember { mutableStateOf(0) }

    var sendEmailResult by remember { mutableStateOf<Result<Unit?>?>(null) }

    val repeatSendEmailTimer = object: CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            repeatSendEmailSeconds = (millisUntilFinished / 1000).toInt()
        }

        override fun onFinish() {
            repeatSendEmailSeconds = 0
        }
    }

    var state = getSecurityEmailState(
        email = userResult.data?.email,
        emailVerification = userResult.data?.emailVerification ?: false
    )

    viewModel.responseSendEmailResult.onEach { result ->
        sendEmailResult = result
    }.launchWhenStarted()

    viewModel.responseUserResult.onEach {
        userResult = it
    }.launchWhenStarted()

    OnLifecycleEvent { _, event ->  
        if(event == Lifecycle.Event.ON_START) {
            viewModel.getUser()
        }
    }

    LaunchedEffect(key1 = sendEmailResult, block = {
        when(sendEmailResult) {
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = content.getString(R.string.error)
                )
                viewModel.responseSendEmailResultToNull()
            }
            is Result.Loading -> Unit
            is Result.Success -> {
                viewModel.getUser()
                repeatSendEmailTimer.start()

                scaffoldState.snackbarHostState.showSnackbar(
                    message = content.getString(R.string.send_email_success)
                )
                viewModel.responseSendEmailResultToNull()
            }
            null -> Unit
        }
    })

    SettingsEmailScreen(
        scaffoldState = scaffoldState,
        userResult = userResult,
        state = state,
        repeatSendEmailSeconds = repeatSendEmailSeconds,
        sendEmailResult = sendEmailResult,
        onBackScreen = onBackScreen,
        updateEmail = {
            viewModel.updateEmail(it)
        },
        addEmail = {
            viewModel.updateEmail(it)
        },
        sendEmailVerification = {
            viewModel.emailVerification()
        },
        onStateChange = {
            state = it
        },
        checkEmail = {
            viewModel.getUser()
        }
    )
}

@Composable
private fun SettingsEmailScreen(
    scaffoldState: ScaffoldState,
    userResult: Result<UserDetails>,
    state: SecurityEmailState?,
    repeatSendEmailSeconds: Int?,
    sendEmailResult: Result<Unit?>?,
    onBackScreen: () -> Unit,
    updateEmail: (email: String) -> Unit,
    addEmail: (email: String) -> Unit,
    sendEmailVerification: () -> Unit,
    onStateChange: (SecurityEmailState) -> Unit,
    checkEmail: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.email),
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
        when(userResult){
            is Result.Error -> ErrorUi()
            is Result.Loading -> LoadingUi()
            is Result.Success -> {
                if(state != null){
                    Success(
                        contentPadding = paddingValues,
                        state = state,
                        user = userResult.data!!,
                        repeatSendEmailSeconds = repeatSendEmailSeconds,
                        updateEmail = updateEmail,
                        addEmail = addEmail,
                        sendEmailVerification = sendEmailVerification,
                        sendEmailResult = sendEmailResult,
                        onStateChange = onStateChange,
                        checkEmail = checkEmail
                    )
                }else{
                    ErrorUi()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Success(
    contentPadding: PaddingValues,
    state: SecurityEmailState,
    user: UserDetails,
    repeatSendEmailSeconds: Int?,
    updateEmail: (email: String) -> Unit,
    addEmail: (email: String) -> Unit,
    sendEmailVerification: () -> Unit,
    sendEmailResult: Result<Unit?>?,
    onStateChange: (SecurityEmailState) -> Unit,
    checkEmail: () -> Unit
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val pagerState = rememberPagerState()

    LaunchedEffect(key1 = sendEmailResult, block = {
        if(
            state == SecurityEmailState.INSECURITY && sendEmailResult is Result.Success
        ){
            pagerState.animateScrollToPage(PAGER_INDEX_EMAIL_VERIFICATION)
        }
    })

    LaunchedEffect(key1 = state, block = {
        when(state) {
            SecurityEmailState.SECURITY -> {
                pagerState.animateScrollToPage(PAGER_INDEX_EMAIL_SECURITY)
            }
            SecurityEmailState.EMAIL_VERIFICATION -> {
                pagerState.animateScrollToPage(PAGER_INDEX_EMAIL_VERIFICATION)
            }
            SecurityEmailState.INSECURITY -> {
                pagerState.animateScrollToPage(PAGER_INDEX_EMAIL_INSECURITY)
            }
        }
    })

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            BaseLottieAnimation(
                type = LottieAnimationType.EMAIL,
                modifier = Modifier
                    .padding(5.dp)
                    .width((screenWidthDp / 1.5).dp)
                    .height((screenHeightDp / 3).dp)
            )
        }

        item {
            HorizontalPager(
                state = pagerState,
                count = PAGER_INDEX_EMAIL_SIZE,
                userScrollEnabled = false
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when(page) {
                        PAGER_INDEX_EMAIL_SECURITY -> EmailSecurity(
                            user = user,
                            updateEmail = updateEmail
                        )
                        PAGER_INDEX_EMAIL_VERIFICATION -> EmailVerification(
                            email = user.email,
                            repeatSendEmailSeconds = repeatSendEmailSeconds,
                            sendEmailVerification = sendEmailVerification,
                            checkEmail = checkEmail,
                            editEmailPage = {
                                onStateChange(SecurityEmailState.INSECURITY)
                            }
                        )
                        PAGER_INDEX_EMAIL_INSECURITY -> EmailInsecurity(
                            addEmail = addEmail
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmailSecurity(
    user: UserDetails,
    updateEmail: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    val emailValidation = emailValidation(email)

    LaunchedEffect(key1 = user.email, block = {
        user.email?.let { email = it }
    })

    Text(
        text = stringResource(id = R.string.update_email_body),
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

    AnimatedVisibility(visible = emailValidation.first) {
        TextButton(onClick = {
            if(emailValidation.first){
                updateEmail(email.trim())
            }
        }) {
            Text(
                text = stringResource(id = R.string.update),
                color = PgkTheme.colors.tintColor,
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

@Composable
private fun EmailVerification(
    email: String?,
    repeatSendEmailSeconds: Int?,
    sendEmailVerification: () -> Unit,
    editEmailPage: () -> Unit,
    checkEmail: () -> Unit
) {
    val sendEmailTextButton = if(repeatSendEmailSeconds == null || repeatSendEmailSeconds == 0)
        stringResource(id = R.string.send_email)
    else
        "${stringResource(id = R.string.repeat_send_email)} ($repeatSendEmailSeconds)"

    Text(
        text = stringResource(id = R.string.verification_email_body),
        color = PgkTheme.colors.primaryText,
        style = PgkTheme.typography.body,
        fontFamily = PgkTheme.fontFamily.fontFamily,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(5.dp)
    )

    TextButton(onClick = {
        if(repeatSendEmailSeconds == null || repeatSendEmailSeconds == 0) {
            sendEmailVerification()
        }
    }) {
        Text(
            text = sendEmailTextButton,
            color = PgkTheme.colors.tintColor,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }

    TextButton(onClick = checkEmail) {
        Text(
            text = stringResource(id = R.string.check_email),
            color = PgkTheme.colors.tintColor,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }

    TextButton(onClick = editEmailPage) {
        Text(
            text = "${stringResource(id = R.string.edit_email)} ($email)",
            color = PgkTheme.colors.tintColor,
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

@Composable
private fun EmailInsecurity(
    addEmail: (email: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    val emailValidation = emailValidation(email)

    Text(
        text = stringResource(id = R.string.add_email_body),
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

    AnimatedVisibility(visible = emailValidation.first) {
        TextButton(onClick = {
            if(emailValidation.first){
                addEmail(email.trim())
            }
        }) {
            Text(
                text = stringResource(id = R.string.add),
                color = PgkTheme.colors.tintColor,
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