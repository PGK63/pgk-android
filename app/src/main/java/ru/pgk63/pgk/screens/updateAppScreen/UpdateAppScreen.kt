package ru.pgk63.pgk.screens.updateAppScreen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.BaseLottieAnimation
import ru.pgk63.core_ui.view.LottieAnimationType
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.firstproject.core_services.remoteConfig.model.LastVersionApp
import ru.pgk63.core_common.extension.parseToBaseDateFormat
import ru.pgk63.pgk.utils.download.Download


object UpdateAppDestination : NavigationDestination {
    override val route: String = "update_app_screen"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.updateAppNavigation(
    onBackScreen: () -> Unit,
) {

    composable(UpdateAppDestination.route) {
        UpdateAppRoute(
            onBackScreen = onBackScreen
        )
    }
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun UpdateAppRoute(
    viewModel: UpdateAppViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
) {
    val context = LocalContext.current

    var lastVersionApp by remember { mutableStateOf<LastVersionApp?>(null) }

    var downloadText by remember { mutableStateOf("") }
    var isDownloading by remember { mutableStateOf(false) }
    var progressDownload by remember { mutableStateOf<Int?>(null) }

    val downloadApk = remember { Download(context = context) }

    viewModel.lastVersionApp.onEach {
        lastVersionApp = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getAppVersion()
    })

    UpdateAppScreen(
        lastVersionApp = lastVersionApp,
        downloadText = downloadText,
        progressDownload = progressDownload,
        isDownloading = isDownloading,
        onBackScreen = onBackScreen,
        updateApp = {
            CoroutineScope(Dispatchers.IO).launch {
                lastVersionApp?.urlApk?.let { url ->
                    try {
                        downloadApk.enqueueDownload(
                            url = url,
                            isDownloading = {
                                isDownloading = it

                                if(!isDownloading){
                                    downloadText = ""
                                    progressDownload = null
                                }
                            },
                            onProgress = {
                                downloadText = "$it/100 %"
                                progressDownload = it
                            }
                        )
                    }catch (e:Exception){
                        downloadText = context.getString(R.string.error)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UpdateAppScreen(
    lastVersionApp: LastVersionApp?,
    downloadText: String,
    progressDownload: Int?,
    isDownloading: Boolean,
    onBackScreen: () -> Unit,
    updateApp: () -> Unit,
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val permissions = rememberMultiplePermissionsState(permissions = listOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.REQUEST_INSTALL_PACKAGES
    ))

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                onBackClick = onBackScreen,
                scrollBehavior = scrollBehavior,
                title = stringResource(id = R.string.updates_available_app)
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
            if(lastVersionApp != null){
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {

                        BaseLottieAnimation(
                            type = LottieAnimationType.UPDATE_APP,
                            modifier = Modifier
                                .padding(5.dp)
                                .width((screenWidthDp / 1.5).dp)
                                .height((screenHeightDp / 3).dp)
                        )

                        Text(
                            text = "${stringResource(id = R.string.version)} " +
                                    "${lastVersionApp.versionName}\n" +
                                    "${stringResource(id = R.string.assembly_code)} " +
                                    "${lastVersionApp.versionCode}\n" +
                                    "${stringResource(id = R.string.date)} ${lastVersionApp.date.parseToBaseDateFormat()}",
                            color = PgkTheme.colors.primaryText,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                            style = PgkTheme.typography.body,
                            fontWeight = FontWeight.W900,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        if(lastVersionApp.title.isNotEmpty()){
                            Text(
                                text = lastVersionApp.title,
                                color = PgkTheme.colors.primaryText,
                                fontFamily = PgkTheme.fontFamily.fontFamily,
                                style = PgkTheme.typography.body,
                                fontWeight = FontWeight.W900
                            )

                            Spacer(modifier = Modifier.height(5.dp))
                        }

                        if(downloadText.isNotEmpty()){
                            Text(
                                text = downloadText,
                                color = PgkTheme.colors.tintColor,
                                fontFamily = PgkTheme.fontFamily.fontFamily,
                                style = PgkTheme.typography.body,
                                fontWeight = FontWeight.W900
                            )

                            if(progressDownload == null){
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }

                        AnimatedVisibility(visible = progressDownload != null) {
                            LinearProgressIndicator(
                                progress = (progressDownload!!.toFloat() / 100),
                                color = PgkTheme.colors.tintColor,
                                modifier = Modifier.padding(5.dp)
                            )
                        }

                        if(lastVersionApp.description.isNotEmpty()){
                            Text(
                                text = lastVersionApp.description,
                                color = PgkTheme.colors.primaryText,
                                fontFamily = PgkTheme.fontFamily.fontFamily,
                                style = PgkTheme.typography.body,
                                fontWeight = FontWeight.W300,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(15.dp))
                        }

                        AnimatedVisibility(visible = !isDownloading) {
                            Button(
                                onClick = {
                                    if(permissions.allPermissionsGranted){
                                        updateApp()
                                    }else {
                                        permissions.launchMultiplePermissionRequest()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                shape = PgkTheme.shapes.cornersStyle,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = PgkTheme.colors.tintColor
                                )
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.update_app)} " +
                                            "(${lastVersionApp.apkSizeMegabytes} MB)",
                                    color = PgkTheme.colors.primaryText,
                                    fontFamily = PgkTheme.fontFamily.fontFamily,
                                    style = PgkTheme.typography.body
                                )
                            }
                        }
                    }
                }
            }else {
                LoadingUi()
            }
        }
    )
}