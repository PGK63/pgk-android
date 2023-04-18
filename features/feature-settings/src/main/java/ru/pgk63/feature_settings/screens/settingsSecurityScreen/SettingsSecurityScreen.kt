package ru.pgk63.feature_settings.screens.settingsSecurityScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_settings.screens.settingsSecurityScreen.viewModel.SettingsSecurityViewModel
import ru.pgk63.feature_settings.view.SettingsButton

@Composable
internal fun SettingsSecurityRoute(
    viewModel: SettingsSecurityViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onChangePasswordScreen: () -> Unit,
    onSettingsEmailScreen: () -> Unit,
    onSettingsTelegramScreen: () -> Unit
) {

    SettingsSecurityScreen(
        onBackScreen = onBackScreen,
        onChangePasswordScreen = onChangePasswordScreen,
        onSettingsEmailScreen = onSettingsEmailScreen,
        onSettingsTelegramScreen = onSettingsTelegramScreen,
        onSignOutApp = {
            viewModel.signOutApp()
        },
        onSignOutAll = {
            viewModel.signOutAll()
        }
    )
}

@Composable
private fun SettingsSecurityScreen(
    onSignOutApp: () -> Unit,
    onSignOutAll: () -> Unit,
    onBackScreen: () -> Unit,
    onChangePasswordScreen: () -> Unit,
    onSettingsEmailScreen: () -> Unit,
    onSettingsTelegramScreen: () -> Unit,
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.security),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            item {

                SecurityUi(
                    onChangePasswordScreen = onChangePasswordScreen,
                    onSettingsEmailScreen = onSettingsEmailScreen,
                    onSettingsTelegramScreen = onSettingsTelegramScreen
                )

                Spacer(modifier = Modifier.height(25.dp))

                DangerZone(
                    onSignOutApp = onSignOutApp,
                    onSignOutAll = onSignOutAll
                )
            }

            item {
                Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
            }
        }
    }
}

@Composable
private fun SecurityUi(
    onChangePasswordScreen: () -> Unit,
    onSettingsEmailScreen: () -> Unit,
    onSettingsTelegramScreen: () -> Unit,
){

    Column {
        SettingsButton(
            title = stringResource(id = R.string.change_password),
            body = stringResource(id = R.string.change_password_body)
        ) { onChangePasswordScreen() }

        Spacer(modifier = Modifier.height(10.dp))

        SettingsButton(
            title = stringResource(id = R.string.email),
            body = stringResource(id = R.string.email_password_body)
        ) { onSettingsEmailScreen() }

        Spacer(modifier = Modifier.height(10.dp))

        SettingsButton(
            title = stringResource(id = R.string.telegram),
            body = stringResource(id = R.string.telegram_password_body)
        ) { onSettingsTelegramScreen() }


        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun DangerZone(
    onSignOutApp:() -> Unit,
    onSignOutAll: () -> Unit
) {
    Column {
        Divider(color = PgkTheme.colors.errorColor, modifier = Modifier.padding(2.dp))
        Text(
            text = stringResource(id = R.string.danger_zone),
            color = PgkTheme.colors.errorColor,
            style = PgkTheme.typography.caption,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(start = 5.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        SettingsButton(
            title = stringResource(id = R.string.sign_out_app),
            body = stringResource(id = R.string.sign_out_app_body),
            onClick = onSignOutApp
        )

        SettingsButton(
            title = stringResource(id = R.string.sign_out_all),
            body = stringResource(id = R.string.sign_out_all_body),
            onClick = onSignOutAll
        )

        Spacer(modifier = Modifier.height(5.dp))

        Divider(color = PgkTheme.colors.errorColor, modifier = Modifier.padding(5.dp))
    }
}