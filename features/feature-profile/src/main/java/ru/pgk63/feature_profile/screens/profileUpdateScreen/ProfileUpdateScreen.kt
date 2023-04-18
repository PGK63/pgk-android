package ru.pgk63.feature_profile.screens.profileUpdateScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.compose.CABINET_MASK
import ru.pgk63.core_common.compose.MaskVisualTransformation
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TextFieldBase
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_profile.screens.profileUpdateScreen.model.ProfileUpdateType
import ru.pgk63.feature_profile.screens.profileUpdateScreen.viewModel.ProfileUpdateViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun ProfileUpdateRoute(
    viewModel: ProfileUpdateViewModel = hiltViewModel(),
    type: ProfileUpdateType,
    onBackScreen: () -> Unit
) {
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()

    var cabinet by remember { mutableStateOf("") }
    var information by remember { mutableStateOf("") }

    var updateResult by remember { mutableStateOf<Result<Unit?>?>(null) }

    viewModel.responseCabinet.onEach {
        cabinet = it.replace("/","")
    }.launchWhenStarted()

    viewModel.responseInformation.onEach {
        information = it
    }.launchWhenStarted()

    viewModel.responseUpdateResult.onEach {
        updateResult = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getUser()
    })

    LaunchedEffect(key1 = updateResult, block = {
        when(updateResult){
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
            }
            is Result.Loading -> Unit
            is Result.Success -> onBackScreen()
            null -> Unit
        }
    })

    ProfileUpdateScreen(
        type = type,
        scaffoldState = scaffoldState,
        cabinet = cabinet,
        information = information,
        onCabinetChange = { viewModel.cabinetChange(it) },
        onInformationChange = { viewModel.informationChange(it) },
        onBackScreen = onBackScreen,
        onUpdate = {
            when(type){
                ProfileUpdateType.CABINET -> viewModel.updateCabinet(cabinet)
                ProfileUpdateType.INFORMATION -> viewModel.updateInformation(information)
            }
        }
    )
}

@Composable
private fun ProfileUpdateScreen(
    scaffoldState: ScaffoldState,
    type: ProfileUpdateType,
    cabinet: String,
    information: String,
    onCabinetChange: (String) -> Unit,
    onInformationChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onUpdate: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {

            val title = when (type) {
                ProfileUpdateType.CABINET -> stringResource(id = R.string.set_cabinet)
                ProfileUpdateType.INFORMATION -> stringResource(id = R.string.set_information)
            }

            TopBarBack(
                title = title,
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
                actions = {
                    TextButton(onClick = onUpdate) {
                        Text(
                            text = stringResource(id = R.string.update),
                            color = PgkTheme.colors.tintColor,
                            style = PgkTheme.typography.body,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                            modifier = Modifier.padding(5.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                if(type == ProfileUpdateType.CABINET) {
                    UpdateColumnProfileUi(
                        value = cabinet,
                        onValueChange = onCabinetChange,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        label = stringResource(id = R.string.cabinet),
                        body = stringResource(id = R.string.cabinet_body),
                        textStyle = PgkTheme.typography.heading,
                        visualTransformation = MaskVisualTransformation(CABINET_MASK),
                        maxChar = 4,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Number
                        )
                    )
                }else if(type == ProfileUpdateType.INFORMATION) {
                    UpdateColumnProfileUi(
                        value = information,
                        onValueChange = onInformationChange,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        label = stringResource(id = R.string.information),
                        body = stringResource(id = R.string.information_guide_body),
                        textStyle = PgkTheme.typography.body
                    )
                }
            }
        }
    }
}

@Composable
private fun UpdateColumnProfileUi(
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxChar: Int? = null,
    value: String,
    label: String,
    body: String,
    onValueChange: (String) -> Unit
) {
    val fieldFocusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit, block = {
        fieldFocusRequester.requestFocus()
    })

    TextFieldBase(
        text = value,
        onTextChanged = onValueChange,
        modifier = modifier,
        label = label,
        singleLine = false,
        textStyle = textStyle,
        modifierTextField = Modifier
            .fillMaxWidth()
            .focusRequester(fieldFocusRequester),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        maxChar = maxChar
    )

    Text(
        text = body,
        color = PgkTheme.colors.primaryText,
        style = PgkTheme.typography.caption,
        fontFamily = PgkTheme.fontFamily.fontFamily,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}