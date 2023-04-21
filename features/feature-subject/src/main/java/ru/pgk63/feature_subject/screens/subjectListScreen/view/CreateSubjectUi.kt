package ru.pgk63.feature_subject.screens.subjectListScreen.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.pgk63.core_common.validation.nameValidation
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TextFieldBase

@Composable
internal fun CreateSubjectDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    onCreateSubjectButtonClick: (ru.pgk63.core_model.subject.CreateSubjectBody) -> Unit
) {
    val focusManager = LocalFocusManager.current

    if(show) {

        var title by remember { mutableStateOf("") }
        val titleValidation = nameValidation(title)

        AlertDialog(
            backgroundColor = PgkTheme.colors.secondaryBackground,
            onDismissRequest = onDismissRequest,
            shape = AbsoluteRoundedCornerShape(15.dp),
            title = {
                Text(
                    text = stringResource(id = R.string.add_subject),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            buttons = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFieldBase(
                        text = title,
                        onTextChanged = { title = it },
                        modifier = Modifier.padding(5.dp),
                        errorText = if (titleValidation.second != null)
                            stringResource(id = titleValidation.second!!) else null,
                        hasError = !titleValidation.first,
                        label = stringResource(id = R.string.name),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {

                            if(titleValidation.first) {
                                onDismissRequest()
                                onCreateSubjectButtonClick(
                                    ru.pgk63.core_model.subject.CreateSubjectBody(
                                        subjectTitle = title
                                    )
                                )
                            }

                            focusManager.clearFocus()
                        })
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        )
    }
}