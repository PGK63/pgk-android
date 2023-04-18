package ru.pgk63.feature_settings.screens.changePasswordScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.copyToClipboard
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_ui.view.BaseLottieAnimation
import ru.pgk63.core_ui.view.LottieAnimationType
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_settings.screens.changePasswordScreen.viewModel.ChangePasswordViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun ChangePasswordRoute(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
) {
    var newPasswordResult by remember { mutableStateOf<Result<String>?>(null) }

    viewModel.responseNewPassword.onEach { result ->
        newPasswordResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.updatePassword()
    })

    ChangePasswordScreen(
        newPasswordResult = newPasswordResult,
        onBackScreen = onBackScreen
    )
}

@Composable
private fun ChangePasswordScreen(
    newPasswordResult: Result<String>?,
    onBackScreen: () -> Unit
) {
    val context = LocalContext.current

    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.change_password),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                BaseLottieAnimation(
                    type = LottieAnimationType.PASSWORD_SECURITY,
                    modifier = Modifier
                        .padding(10.dp)
                        .width((screenWidthDp / 1.5).dp)
                        .height((screenHeightDp / 3).dp)
                )

                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = stringResource(id = R.string.your_new_password),
                    color = PgkTheme.colors.primaryText,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    style = PgkTheme.typography.body
                )

                Spacer(modifier = Modifier.height(20.dp))

                newPasswordResult?.data?.let { password ->

                    LazyRow(verticalAlignment = Alignment.CenterVertically) {
                        item {
                            Text(
                                text = password,
                                color = PgkTheme.colors.primaryText,
                                fontFamily = PgkTheme.fontFamily.fontFamily,
                                style = PgkTheme.typography.body,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            IconButton(onClick = { context.copyToClipboard(password) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_copy),
                                    contentDescription = null,
                                    tint = PgkTheme.colors.primaryText
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
            }
        }
    }
}