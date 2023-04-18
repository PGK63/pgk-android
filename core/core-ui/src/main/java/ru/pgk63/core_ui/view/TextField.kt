package ru.pgk63.core_ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.pgk63.core_common.validation.emailValidation
import ru.pgk63.core_common.validation.passwordValidation
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.MainTheme
import ru.pgk63.core_ui.theme.PgkTheme

@Composable
fun rememberTextFieldColors(
    textColor: Color = PgkTheme.colors.primaryText,
    focusedIndicatorColor: Color = PgkTheme.colors.tintColor,
    backgroundColor: Color = PgkTheme.colors.primaryBackground,
    cursorColor: Color = PgkTheme.colors.tintColor,
    focusedLabelColor: Color = PgkTheme.colors.tintColor,
    unfocusedLabelColor: Color = PgkTheme.colors.primaryText,
    errorIndicatorColor: Color = PgkTheme.colors.errorColor,
) = TextFieldDefaults.textFieldColors(
    textColor = textColor,
    focusedIndicatorColor = focusedIndicatorColor,
    backgroundColor = backgroundColor,
    cursorColor = cursorColor,
    focusedLabelColor = focusedLabelColor,
    unfocusedLabelColor = unfocusedLabelColor,
    errorIndicatorColor = errorIndicatorColor,
)

@Composable
fun TextFieldBase(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    modifierTextField: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    maxChar: Int? = null,
    hasError: Boolean = false,
    errorText: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = PgkTheme.shapes.cornersStyle,
    textStyle: TextStyle = PgkTheme.typography.body,
    colors: TextFieldColors = rememberTextFieldColors(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            modifier = modifierTextField,
            onValueChange = {
                onTextChanged(if(maxChar != null) it.take(maxChar) else it)
                if (maxChar != null && it.length > maxChar){
                    focusManager.moveFocus(FocusDirection.Down)
                }
            },
            singleLine = singleLine,
            shape = shape,
            label = { label?.let { Text(text = label, color = PgkTheme.colors.primaryText) } },
            placeholder = { placeholder?.let { Text(text = placeholder, color = PgkTheme.colors.primaryText) } },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            colors = colors,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions,
            isError = hasError,
            maxLines = maxLines,
            textStyle = textStyle
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row {

            if(errorText != null && errorText.isNotEmpty()){
                Text(
                    text = errorText,
                    color = PgkTheme.colors.errorColor,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    style = PgkTheme.typography.caption
                )

                Spacer(modifier = Modifier.width(15.dp))
            }

            if(maxChar != null){
                Text(
                    text = "${text.length} / $maxChar",
                    color = PgkTheme.colors.primaryText,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    style = PgkTheme.typography.caption
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldSearch(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = stringResource(id = R.string.search),
    closeVisible: Boolean = true,
    onClose: () -> Unit = {},
    onSearch: (KeyboardActionScope.() -> Unit)? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        onValueChange = onTextChanged,
        modifier = modifier,
        shape = PgkTheme.shapes.cornersStyle,
        colors = rememberTextFieldColors(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch?.let { it() }
            keyboardController?.hide()
        }),
        placeholder = {
            if (label != null) {
                Text(text = label, color = PgkTheme.colors.primaryText)
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = PgkTheme.colors.controlColor
            )
        },
        trailingIcon = {
            if(closeVisible){
                IconButton(onClick = {
                    onClose()
                    keyboardController?.hide()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = PgkTheme.colors.controlColor
                    )
                }
            }
        }
    )
}

@Composable
fun TextFieldEmail(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    validation: Pair<Boolean, Int?> = emailValidation(text),
    label: String = stringResource(id = R.string.email),
    onError: (Boolean) -> Unit = {},
    onNext: (KeyboardActionScope.() -> Unit)? = null
) {
    TextFieldBase(
        text = text,
        onTextChanged = onTextChanged,
        errorText = if(validation.second != null) stringResource(id = validation.second!!) else null,
        hasError = validation.first,
        label = label,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            onError(!validation.first)
            onNext?.let { it() }
        })
    )
}

@Composable
fun TextFieldPassword(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    validation: Pair<Boolean, Int?>? = passwordValidation(text),
    label: String = stringResource(id = R.string.password),
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    var showPassword by remember { mutableStateOf(false) }

    TextFieldBase(
        text = text,
        onTextChanged = onTextChanged,
        maxChar = 256,
        errorText = if(validation?.second != null) stringResource(id = validation.second!!) else null,
        hasError = !(validation?.first ?: true),
        modifier = modifier,
        label = label,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = {
            AnimatedVisibility(text.isNotEmpty()) {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if(showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Visibility",
                        tint = PgkTheme.colors.primaryText
                    )
                }
            }
        }
    )
}

@Preview(showBackground = false)
@Composable
private fun TextFieldDartPreview() {
    MainTheme(darkTheme = true) {
        TextFieldBase(
            text = "Danila",
            errorText = "Invalid username",
            hasError = false,
            onTextChanged = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun TextFieldLightPreview() {
    MainTheme(darkTheme = false) {
        TextFieldBase(
            text = "Danila",
            errorText = "Invalid username",
            hasError = false,
            onTextChanged = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun TextFieldSearchDartPreview() {
    MainTheme(darkTheme = true) {
        TextFieldSearch(
            text = "Danila",
            onTextChanged = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun TextFieldSearchLightPreview() {
    MainTheme(darkTheme = false) {
        TextFieldSearch(
            text = "Danila",
            onTextChanged = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun TextFieldEmailDartPreview() {
    MainTheme(darkTheme = true) {
        TextFieldEmail(
            text = "Danila",
            onTextChanged = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun TextFieldEmailLightPreview() {
    MainTheme(darkTheme = false) {
        TextFieldEmail(
            text = "Danila",
            onTextChanged = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun TextFieldPasswordDartPreview() {
    MainTheme(darkTheme = true) {
        TextFieldPassword(
            text = "Danila",
            onTextChanged = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun TextFieldPasswordLightPreview() {
    MainTheme(darkTheme = false) {
        TextFieldPassword(
            text = "Danila",
            onTextChanged = {}
        )
    }
}
