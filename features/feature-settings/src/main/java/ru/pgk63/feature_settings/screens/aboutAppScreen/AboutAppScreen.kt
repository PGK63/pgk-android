package ru.pgk63.feature_settings.screens.aboutAppScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.firstproject.core_services.remoteConfig.model.LastVersionApp
import ru.pgk63.core_common.extension.parseToBaseDateFormat
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.BaseLottieAnimation
import ru.pgk63.core_ui.view.LottieAnimationType
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_settings.screens.aboutAppScreen.model.CurrentVersionApp
import ru.pgk63.feature_settings.screens.aboutAppScreen.viewModel.AboutAppViewModel

@Composable
internal fun AboutAppRoute(
    viewModel: AboutAppViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onUpdateAppScreen: () -> Unit
) {
    val lastVersionApp = viewModel.lastVersionApp
    val currentVersionApp = viewModel.currentVersionApp

    AboutAppScreen(
        lastVersionApp = lastVersionApp,
        currentVersionApp = currentVersionApp,
        onBackScreen = onBackScreen,
        onUpdateAppScreen = onUpdateAppScreen
    )
}

@Composable
private fun AboutAppScreen(
    lastVersionApp: LastVersionApp?,
    currentVersionApp: CurrentVersionApp,
    onBackScreen: () -> Unit,
    onUpdateAppScreen: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.about_app),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
        ) {
            item {
                BaseLottieAnimation(
                    type = LottieAnimationType.DETAILS_APP,
                    modifier = Modifier
                        .padding(10.dp)
                        .width((screenWidthDp / 1.5).dp)
                        .height((screenHeightDp / 3).dp)
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = PgkTheme.colors.primaryText)) {
                            append(stringResource(id = R.string.version))
                        }

                        withStyle(style = SpanStyle(
                            color = PgkTheme.colors.tintColor,
                            fontWeight = FontWeight.W500
                        )) {
                            append(" ${currentVersionApp.versionName}")
                        }
                    },
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    fontWeight = FontWeight.W300,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = PgkTheme.colors.primaryText)) {
                            append(stringResource(id = R.string.assembly_code))
                        }

                        withStyle(style = SpanStyle(
                            color = PgkTheme.colors.tintColor,
                            fontWeight = FontWeight.W500
                        )) {
                            append(" ${currentVersionApp.versionCode}")
                        }
                    },
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    fontWeight = FontWeight.W300,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = PgkTheme.colors.primaryText)) {
                            append(stringResource(id = R.string.date_installation))
                        }

                        withStyle(style = SpanStyle(
                            color = PgkTheme.colors.tintColor,
                            fontWeight = FontWeight.W500
                        )) {
                            append(" ${currentVersionApp.installDate.parseToBaseDateFormat()}")
                        }
                    },
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    fontWeight = FontWeight.W300,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = PgkTheme.colors.primaryText)) {
                            append(stringResource(id = R.string.date_last_update))
                        }

                        withStyle(style = SpanStyle(
                            color = PgkTheme.colors.tintColor,
                            fontWeight = FontWeight.W500
                        )) {
                            append(" ${currentVersionApp.updateDate.parseToBaseDateFormat()}")
                        }
                    },
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    fontWeight = FontWeight.W300,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                if(lastVersionApp != null && lastVersionApp.isOldVersionApp(currentVersionApp.versionCode)){
                    Button(
                        onClick = onUpdateAppScreen,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        shape = PgkTheme.shapes.cornersStyle,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = PgkTheme.colors.tintColor
                        )
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.updates_available)} ${lastVersionApp.versionName}",
                            color = PgkTheme.colors.primaryText,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                            style = PgkTheme.typography.body,
                            textAlign = TextAlign.Center
                        )
                    }
                }else {
                    Text(
                        text = stringResource(id = R.string.latest_version_app),
                        color = PgkTheme.colors.tintColor,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        fontWeight = FontWeight.W900,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}