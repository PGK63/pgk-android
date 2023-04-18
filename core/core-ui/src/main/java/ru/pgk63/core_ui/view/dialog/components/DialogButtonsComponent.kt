package ru.pgk63.core_ui.view.dialog.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.utils.testTags
import com.maxkeppeler.sheets.core.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.dialog.model.DialogBaseSelection
import ru.pgk63.core_ui.view.dialog.model.DialogButtonStyle
import ru.pgk63.core_ui.view.dialog.model.DialogSelectionButton

/**
 * Header component of the dialog.
 * @param selection The selection configuration for the dialog.
 * @param onPositive Listener that is invoked when the positive button is clicked.
 * @param onNegative Listener that is invoked when the negative button is clicked.
 * @param onPositiveValid If the positive button is valid and therefore enabled.
 */
@ExperimentalMaterial3Api
@Composable
fun DialogButtonsComponent(
    selection: DialogBaseSelection,
    onPositive: () -> Unit,
    onNegative: () -> Unit,
    onClose: () -> Unit,
    onPositiveValid: Boolean = true,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.scd_small_100))
            .padding(horizontal = dimensionResource(id = R.dimen.scd_normal_100)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {

        selection.extraButton?.let {
            SelectionDialogButtonComponent(
                modifier = Modifier
                    .wrapContentWidth(),
                button = selection.extraButton,
                onClick = { selection.onExtraButtonClick?.invoke() },
                testTag = TestTags.BUTTON_EXTRA,
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        SelectionDialogButtonComponent(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.scd_normal_100)),
            button = selection.negativeButton,
            onClick = { onNegative(); onClose() },
            defaultText = stringResource(id = ru.pgk63.core_ui.R.string.cancel),
            testTag = TestTags.BUTTON_NEGATIVE,
        )

        SelectionDialogButtonComponent(
            modifier = Modifier
                .wrapContentWidth(),
            button = selection.positiveButton,
            onClick = { onPositive(); onClose() },
            enabled = onPositiveValid,
            defaultText = stringResource(id = ru.pgk63.core_ui.R.string.ok),
            testTag = TestTags.BUTTON_POSITIVE,
        )
    }
}

/**
 * A helper component to setup the right button.
 * @param modifier The modifier that is applied to the button.
 * @param button The data that is used to build this button.
 * @param onClick Listener that is invoked when the button is clicked.
 * @param enabled Controls the enabled state of this button. When false, this component will not respond to user input, and it will appear visually disabled and disabled to accessibility services.
 * @param defaultText The text that is used by default in the button data does not contain a text.
 * @param testTag The text that is used for the test tag.
 */
@Composable
private fun SelectionDialogButtonComponent(
    modifier: Modifier,
    button: DialogSelectionButton?,
    onClick: () -> Unit,
    enabled: Boolean = true,
    defaultText: String = "",
    testTag: String
) {
    val buttonContent: @Composable RowScope.() -> Unit = {
        button?.icon?.let { icon ->
            DialogIconComponent(
                modifier = Modifier
                    .testTags(testTag, TestTags.BUTTON_ICON)
                    .size(dimensionResource(R.dimen.scd_size_100)),
                iconSource = icon,
                defaultTint = PgkTheme.colors.primaryText
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.scd_small_100)))
        }
        Text(
            text = button?.text ?: defaultText,
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily
        )
    }

    when {
        button == null || button.type == DialogButtonStyle.TEXT -> {
            TextButton(
                modifier = modifier.testTag(testTag),
                onClick = onClick,
                enabled = enabled,
                content = buttonContent
            )
        }
        button.type == DialogButtonStyle.FILLED -> {
            Button(
                modifier = modifier.testTag(testTag),
                onClick = onClick,
                enabled = enabled,
                content = buttonContent
            )
        }
        button.type == DialogButtonStyle.ELEVATED -> {
            ElevatedButton(
                modifier = modifier.testTag(testTag),
                onClick = onClick,
                enabled = enabled,
                content = buttonContent
            )
        }
        button.type == DialogButtonStyle.OUTLINED -> {
            OutlinedButton(
                modifier = modifier.testTag(testTag),
                onClick = onClick,
                enabled = enabled,
                content = buttonContent
            )
        }
    }

}