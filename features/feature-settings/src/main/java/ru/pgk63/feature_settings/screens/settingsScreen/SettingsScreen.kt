package ru.pgk63.feature_settings.screens.settingsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_settings.screens.settingsScreen.enums.SettingsType
import ru.pgk63.feature_settings.view.SettingsButton

@Composable
internal fun SettingsRoute(
    onBackScreen: () -> Unit,
    onSettingsSecurityScreen: () -> Unit,
    onSettingsNotificationsScreen: () -> Unit,
    onSettingsLanguageScreen: () -> Unit,
    onSettingsAppearanceScreen: () -> Unit,
    onSettingsStorageUsageScreen: () -> Unit,
    onAboutAppScreen: () -> Unit
) {
    SettingsScreen(
        onBackScreen = onBackScreen,
        onSettingsSecurityScreen = onSettingsSecurityScreen,
        onSettingsNotificationsScreen = onSettingsNotificationsScreen,
        onSettingsLanguageScreen = onSettingsLanguageScreen,
        onSettingsAppearanceScreen = onSettingsAppearanceScreen,
        onSettingsStorageUsageScreen = onSettingsStorageUsageScreen,
        onAboutAppScreen = onAboutAppScreen
    )
}

@Composable
private fun SettingsScreen(
    onBackScreen: () -> Unit,
    onSettingsSecurityScreen: () -> Unit,
    onSettingsNotificationsScreen: () -> Unit,
    onSettingsLanguageScreen: () -> Unit,
    onSettingsAppearanceScreen: () -> Unit,
    onSettingsStorageUsageScreen: () -> Unit,
    onAboutAppScreen: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.settings),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                SettingsType.values().forEach { type ->
                    SettingsButton(
                        title = stringResource(id = type.textId),
                        body = stringResource(id = type.bodyId),
                        iconId = type.iconId
                    ){
                        when(type){
                            SettingsType.APPEARANCE -> onSettingsAppearanceScreen()
                            SettingsType.STORAGE_USAGE -> onSettingsStorageUsageScreen()
                            SettingsType.NOTIFICATIONS -> onSettingsNotificationsScreen()
                            SettingsType.SECURITY -> onSettingsSecurityScreen()
                            SettingsType.LANGUAGE -> onSettingsLanguageScreen()
                            SettingsType.ABOUT_APP -> onAboutAppScreen()
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