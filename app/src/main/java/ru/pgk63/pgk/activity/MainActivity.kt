package ru.pgk63.pgk.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.language.setLocale
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_navigation.LocalNavController
import ru.pgk63.core_navigation.LocalNavHostController
import ru.pgk63.core_ui.theme.MainTheme
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.feature_auth.navigation.AuthDestination
import ru.pgk63.feature_main.navigation.MainDestination
import ru.pgk63.pgk.navigation.MainNavHost
import ru.pgk63.pgk.services.fcm.FCM.Companion.subscribeToTopics
import ru.pgk63.pgk.utils.ConnectionInternet
import ru.pgk63.core_ui.R
import ru.pgk63.pgk.screens.updateAppScreen.UpdateAppDestination


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("FlowOperatorInvokedInComposition", "UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val connectionInternet = ConnectionInternet(application)
            var checkInternet by remember { mutableStateOf(true) }

            val mainViewModel = hiltViewModel<MainViewModel>()

            var userLocalDatabase by remember { mutableStateOf<UserLocalDatabase?>(UserLocalDatabase()) }

            val firebaseMessaging = remember { FirebaseMessaging.getInstance() }
            var isOldVersionApp by remember { mutableStateOf(false) }

            val navHostController = rememberAnimatedNavController()
            val isSystemInDarkTheme = isSystemInDarkTheme()

            connectionInternet.observe(this){
                checkInternet = it
            }

            mainViewModel.user.onEach {
                userLocalDatabase = it
            }.launchWhenStarted()

            mainViewModel.isOldVersionApp.onEach {
                isOldVersionApp = it
            }.launchWhenStarted()

            LaunchedEffect(key1 = userLocalDatabase, block = {
                if(userLocalDatabase?.languageCode != null){
                    this@MainActivity.setLocale(userLocalDatabase!!.languageCode!!)
                }
            })

            LaunchedEffect(key1 = isOldVersionApp, block = {
                if(isOldVersionApp){
                    delay(1000L)
                    navHostController.navigate(UpdateAppDestination.route)
                }
            })

            Permissions()

            userLocalDatabase?.let { user ->

                subscribeToTopics(
                    firebaseMessaging = firebaseMessaging,
                    user = user
                )

                CompositionLocalProvider(
                    LocalNavHostController provides navHostController,
                    LocalNavController provides navHostController
                ) {
                    MainTheme(
                        darkTheme = user.darkMode ?: isSystemInDarkTheme,
                        style = user.themeStyle,
                        textSize = user.themeFontSize,
                        corners = user.themeCorners,
                        fontFamily = user.themeFontStyle
                    ) {
                        Scaffold(
                            bottomBar = {
                                AnimatedVisibility(visible = !checkInternet) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(PgkTheme.colors.errorColor)
                                    ){
                                        Text(
                                            text = stringResource(id = R.string.error_internet),
                                            color = PgkTheme.colors.primaryText,
                                            style = PgkTheme.typography.body,
                                            fontFamily = PgkTheme.fontFamily.fontFamily,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        ) {
                            MainNavHost(
                                startDestination = if(user.statusRegistration)
                                    MainDestination.route else AuthDestination.route
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun Permissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberMultiplePermissionsState(permissions = listOf(
                Manifest.permission.POST_NOTIFICATIONS
            ))
        } else {
            rememberMultiplePermissionsState(permissions = listOf())
        }

        LaunchedEffect(key1 = Unit, block = {
            permissions.launchMultiplePermissionRequest()
        })
    }
}