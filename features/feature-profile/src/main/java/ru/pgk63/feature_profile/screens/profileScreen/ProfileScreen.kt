package ru.pgk63.feature_profile.screens.profileScreen

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.api.user.model.UserDetails
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.extension.toByteArray
import ru.pgk63.core_common.security.model.getSecurityEmailState
import ru.pgk63.core_common.security.model.getSecurityTelegramState
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.icon.ResIcons
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.ImageCoil
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_profile.screens.profileScreen.viewModel.ProfileViewModel
import ru.pgk63.feature_profile.screens.profileUpdateScreen.model.ProfileUpdateType

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onProfileUpdateScreen: (ProfileUpdateType) -> Unit,
    onUserPageScreen: (UserRole, userId: Int) -> Unit,
    onSettingsEmailScreen: () -> Unit,
    onSettingsTelegramScreen: () -> Unit
) {
    val context = LocalContext.current

    var userResult by remember { mutableStateOf<Result<UserDetails>>(Result.Loading()) }
    var userRole by remember { mutableStateOf<UserRole?>(null) }
    var userNewPhotoUrl by remember { mutableStateOf<String?>(null) }

    val scaffoldState = rememberScaffoldState()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.uploadUserImage(uri.toByteArray(context))
            }
        }
    )

    viewModel.responseUser.onEach { result ->
        userResult = result
    }.launchWhenStarted()

    viewModel.userLocalDatabase.onEach {
        userRole = it?.userRole
    }.launchWhenStarted()

    viewModel.responseUpdateUserImageUrl.onEach { url ->
        if(url != null){

            userNewPhotoUrl = url

            scaffoldState.snackbarHostState.showSnackbar(
                message = context.getString(R.string.photo_updated)
            )
        }
    }.launchWhenStarted()

    ProfileScreen(
        scaffoldState = scaffoldState,
        userNewPhotoUrl = userNewPhotoUrl,
        userResult = userResult,
        userRole = userRole,
        onBackScreen = onBackScreen,
        onProfileUpdateScreen = onProfileUpdateScreen,
        onSettingsEmailScreen = onSettingsEmailScreen,
        onSettingsTelegramScreen = onSettingsTelegramScreen,
        onUserPageScreen = {
            userResult.data?.id?.let { userId ->
                when(userRole){
                    UserRole.STUDENT, UserRole.HEADMAN, UserRole.DEPUTY_HEADMAN -> {
                        onUserPageScreen(UserRole.STUDENT, userId)
                    }
                    UserRole.TEACHER -> onUserPageScreen(UserRole.TEACHER, userId)
                    UserRole.DEPARTMENT_HEAD -> onUserPageScreen(UserRole.DEPARTMENT_HEAD, userId)
                    UserRole.DIRECTOR -> onUserPageScreen(UserRole.DIRECTOR, userId)
                    else -> Unit
                }
            }
        },
        updateUserPhoto = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    )
}

@Composable
private fun ProfileScreen(
    scaffoldState: ScaffoldState,
    userResult: Result<UserDetails>,
    userRole: UserRole?,
    userNewPhotoUrl: String?,
    onBackScreen: () -> Unit,
    onProfileUpdateScreen: (ProfileUpdateType) -> Unit,
    onUserPageScreen: () -> Unit,
    onSettingsEmailScreen: () -> Unit,
    onSettingsTelegramScreen: () -> Unit,
    updateUserPhoto: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            Column {
                TopBarBack(
                    title = stringResource(id = R.string.profile),
                    scrollBehavior = scrollBehavior,
                    onBackClick = onBackScreen,
                    actions = {
                        if(
                            userRole == UserRole.STUDENT ||
                            userRole == UserRole.HEADMAN ||
                            userRole == UserRole.DEPUTY_HEADMAN ||
                            userRole == UserRole.TEACHER ||
                            userRole == UserRole.DEPARTMENT_HEAD ||
                            userRole == UserRole.DIRECTOR
                        ) {
                            TextButton(onClick = onUserPageScreen) {
                                Text(
                                    text = stringResource(id = R.string.page),
                                    color = PgkTheme.colors.tintColor,
                                    style = PgkTheme.typography.body,
                                    fontFamily = PgkTheme.fontFamily.fontFamily,
                                    modifier = Modifier.padding(5.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                )

                AnimatedVisibility(
                    visible = userResult.data != null,
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    TopBarUserInfo(
                        user = userResult.data!!,
                        userRole = userRole,
                        userNewPhotoUrl = userNewPhotoUrl
                    )
                }
            }
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
        when(userResult){
            is Result.Error -> ErrorUi(message = userResult.message)
            is Result.Loading -> LoadingUi()
            is Result.Success -> UserSuccess(
                contentPadding = paddingValues,
                user = userResult.data!!,
                userRole = userRole,
                updateUserPhoto = updateUserPhoto,
                onProfileUpdateScreen = onProfileUpdateScreen,
                onSettingsEmailScreen = onSettingsEmailScreen,
                onSettingsTelegramScreen = onSettingsTelegramScreen,
            )
        }
    }
}

@Composable
private fun TopBarUserInfo(
    user: UserDetails,
    userRole: UserRole?,
    userNewPhotoUrl: String?,
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        modifier = Modifier.fillMaxWidth(),
        shape = AbsoluteRoundedCornerShape(
            0, 0, 5, 5
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            if(user.photoUrl != null || userNewPhotoUrl != null) {
                ImageCoil(
                    url = userNewPhotoUrl ?: user.photoUrl,
                    modifier = Modifier
                        .width((screenWidthDp / 2).dp)
                        .height((screenHeightDp / 4.3).dp)
                )
            }else {
                Image(
                    painter = painterResource(id = R.drawable.profile_photo),
                    contentDescription = null,
                    modifier = Modifier
                        .width((screenWidthDp / 2).dp)
                        .height((screenHeightDp / 4.3).dp)
                )
            }

            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${user.lastName} ${user.firstName} " +
                            (user.middleName ?: ""),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )

                if(userRole != null){
                    Text(
                        text = stringResource(id = userRole.nameId),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun UserSuccess(
    contentPadding: PaddingValues,
    user: UserDetails,
    userRole: UserRole?,
    updateUserPhoto: () -> Unit,
    onProfileUpdateScreen: (ProfileUpdateType) -> Unit,
    onSettingsEmailScreen: () -> Unit,
    onSettingsTelegramScreen: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        item {

            Spacer(modifier = Modifier.height(20.dp))

            ButtonCard(
                painter = painterResource(id = ResIcons.camera),
                text = stringResource(id = R.string.set_profile_photo),
                onClick = updateUserPhoto
            )

            if(
                userRole == UserRole.TEACHER
                || userRole== UserRole.DEPARTMENT_HEAD
                || userRole == UserRole.DIRECTOR
            ) {
                ButtonCard(
                    painter = painterResource(id = ResIcons.classroom),
                    text = stringResource(id = R.string.set_cabinet),
                    onClick = { onProfileUpdateScreen(ProfileUpdateType.CABINET) }
                )

                ButtonCard(
                    painter = painterResource(id = ResIcons.description),
                    text = stringResource(id = R.string.set_information),
                    onClick = { onProfileUpdateScreen(ProfileUpdateType.INFORMATION) }
                )
            }

            SecurityUi(
                email = user.email,
                emailVerification = user.emailVerification,
                telegramId = user.telegramId,
                onSettingsEmailScreen = onSettingsEmailScreen,
                onSettingsTelegramScreen = onSettingsTelegramScreen
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ButtonCard(
    painter: Painter,
    text: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        shape = PgkTheme.shapes.cornersStyle,
        backgroundColor = PgkTheme.colors.secondaryBackground,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.padding(10.dp).size(25.dp),
                tint = PgkTheme.colors.primaryText
            )

            Text(
                text = text,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SecurityUi(
    telegramId: Int?,
    email: String?,
    emailVerification: Boolean,
    onSettingsEmailScreen: () -> Unit,
    onSettingsTelegramScreen: () -> Unit,
) {
    val securityEmailState = getSecurityEmailState(
        email = email,
        emailVerification = emailVerification
    )

    val securityTelegramState = getSecurityTelegramState(
        telegramId = telegramId
    )

    Column {
        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = stringResource(id = R.string.security),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.heading,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Divider(color = PgkTheme.colors.secondaryBackground)

        SecurityItem(
            painter = painterResource(id = securityEmailState.iconId),
            text = stringResource(id = securityEmailState.nameId),
            onClick = { onSettingsEmailScreen() }
        )

        Divider(color = PgkTheme.colors.secondaryBackground)

        SecurityItem(
            painter = painterResource(id = securityTelegramState.iconId),
            text = stringResource(id = securityTelegramState.nameId),
            onClick = { onSettingsTelegramScreen() }
        )

        Divider(color = PgkTheme.colors.secondaryBackground)
    }
}

@Composable
private fun SecurityItem(
    painter: Painter,
    text:String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .size(25.dp)
        )

        Text(
            text = text,
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}