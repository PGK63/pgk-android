package ru.pgk63.feature_auth.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.api.auth.model.SignIn
import ru.pgk63.core_common.api.auth.model.SignInResponse
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.feature_auth.screens.auth.viewModel.AuthViewModel
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.MainTheme
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.validation.nameValidation
import ru.pgk63.core_common.validation.passwordValidation
import ru.pgk63.core_ui.view.*

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun AuthRoute(
    viewModel: AuthViewModel = hiltViewModel(),
    onForgotPasswordScreen: () -> Unit
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var firstNameValidation by rememberSaveable { mutableStateOf<Pair<Boolean, Int?>?>(null) }
    var lastNameValidation by rememberSaveable { mutableStateOf<Pair<Boolean, Int?>?>(null) }
    var passwordValidation by rememberSaveable { mutableStateOf<Pair<Boolean, Int?>?>(null) }

    var resultSignIn by remember { mutableStateOf<Result<SignInResponse>?>(null) }

    viewModel.responseSignIn.onEach { result ->
        resultSignIn = result
    }.launchWhenStarted()

    AuthScreen(
        firstName = firstName,
        lastName = lastName,
        password = password,
        firstNameValidation = firstNameValidation ,
        lastNameValidation = lastNameValidation,
        passwordValidation = passwordValidation,
        resultSignIn = resultSignIn,
        signIn = {

            firstNameValidation = nameValidation(firstName)
            lastNameValidation = nameValidation(lastName)
            passwordValidation = passwordValidation(password)

            if(firstNameValidation?.first == true &&
                lastNameValidation?.first == true &&
                passwordValidation?.first == true){
                val body = SignIn(
                    firstName = firstName,
                    lastName = lastName,
                    password = password
                )

                viewModel.signIn(body = body)
            }
        },
        onFirstNameChange = { firstName = it },
        onLastNameChange = { lastName = it },
        onPasswordChange = { password = it },
        onForgotPasswordScreen = onForgotPasswordScreen
    )
}

@Composable
private fun AuthScreen(
    firstName: String,
    lastName: String,
    password: String,
    firstNameValidation: Pair<Boolean, Int?>? = nameValidation(firstName),
    lastNameValidation: Pair<Boolean, Int?>? = nameValidation(lastName),
    passwordValidation: Pair<Boolean, Int?>? = passwordValidation(password),
    resultSignIn: Result<SignInResponse>? = null,
    signIn: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotPasswordScreen: () -> Unit = {}
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PgkTheme.colors.primaryBackground
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                BaseLottieAnimation(
                    type = LottieAnimationType.WELCOME,
                    modifier = Modifier
                        .padding(5.dp)
                        .width((screenWidthDp / 1.5).dp)
                        .height((screenHeightDp / 2).dp)
                )

                Text(
                    text = stringResource(id = R.string.welcome),
                    color = PgkTheme.colors.primaryText,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    style = PgkTheme.typography.heading,
                    modifier = Modifier.padding(5.dp)
                )

                if (resultSignIn is Result.Loading) {
                    CircularProgressIndicator(
                        color = PgkTheme.colors.tintColor,
                        modifier = Modifier.padding(5.dp)
                    )
                } else if (resultSignIn is Result.Error || resultSignIn?.data?.errorMessage != null) {
                    Text(
                        text = resultSignIn.data?.errorMessage
                            ?: stringResource(id = R.string.authorization_error),
                        color = PgkTheme.colors.errorColor,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        style = PgkTheme.typography.body,
                        modifier = Modifier.padding(5.dp)
                    )
                }

                TextFieldBase(
                    text = firstName,
                    onTextChanged = onFirstNameChange,
                    maxChar = 256,
                    label = stringResource(id = R.string.firstName),
                    modifier = Modifier.padding(5.dp),
                    errorText = if (firstNameValidation?.second != null)
                        stringResource(id = firstNameValidation.second!!) else null,
                    hasError = !(firstNameValidation?.first ?: true),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                TextFieldBase(
                    text = lastName,
                    onTextChanged = onLastNameChange,
                    maxChar = 256,
                    label = stringResource(id = R.string.lastName),
                    modifier = Modifier.padding(5.dp),
                    errorText = if (lastNameValidation?.second != null)
                        stringResource(id = lastNameValidation.second!!) else null,
                    hasError = !(lastNameValidation?.first ?: true),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                Column {
                    TextFieldPassword(
                        text = password,
                        onTextChanged = onPasswordChange,
                        modifier = Modifier.padding(
                            top = 5.dp,
                            start = 5.dp,
                            end = 5.dp
                        ),
                        validation = passwordValidation,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        })
                    )

                    TextButton(
                        modifier = Modifier
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                bottom = 5.dp
                            )
                            .align(Alignment.End),
                        onClick = onForgotPasswordScreen
                    ) {
                        Text(
                            text = stringResource(id = R.string.forgot_password),
                            color = PgkTheme.colors.primaryText,
                            style = PgkTheme.typography.caption,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    NextButton(
                        text = stringResource(id = R.string.entrance),
                        onClick = signIn
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Preview(device = Devices.PIXEL, showSystemUi = true, showBackground = true)
@Composable
private fun AuthScreenDartPreview(){
    MainTheme(darkTheme = true) {
        AuthScreen(
            firstName = "",
            lastName = "",
            password = "",
            signIn = {},
            onFirstNameChange = {},
            onLastNameChange = {},
            onPasswordChange = {}
        )
    }
}


@Preview(device = Devices.PIXEL, showSystemUi = true, showBackground = true)
@Composable
private fun AuthScreenLightPreview(){
    MainTheme(darkTheme = false) {
        AuthScreen(
            firstName = "",
            lastName = "",
            password = "",
            signIn = {},
            onFirstNameChange = {},
            onLastNameChange = {},
            onPasswordChange = {}
        )
    }
}
