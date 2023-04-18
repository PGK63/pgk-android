package ru.pgk63.core_ui.view.dialog

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.pgk63.core_ui.theme.PgkTheme

/**
 * Base component for a dialog.
 * @param state The state of the sheet.
 * @param properties DialogProperties for further customization of this dialog's behavior.
 * @param onDialogClick Listener that is invoked when the dialog was clicked.
 * @param content The content to be displayed inside the dialog.
 */
@Composable
fun DialogBase(
    state: DialogSheetState = rememberSheetState(true),
    properties: DialogProperties = DialogProperties(),
    onDialogClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(Unit) {
        state.markAsEmbedded()
    }

    if (!state.visible) return

    val boxInteractionSource = remember { MutableInteractionSource() }
    val contentInteractionSource = remember { MutableInteractionSource() }

    Dialog(
        onDismissRequest = state::dismiss,
        properties = properties,
    ) {

        // Quick-fix for issue
        // https://stackoverflow.com/questions/71285843/how-to-make-dialog-re-measure-when-a-child-size-changes-dynamically/71287474#71287474

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = boxInteractionSource,
                    indication = null,
                    onClick = state::dismiss
                )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .clickable(
                        indication = null,
                        interactionSource = contentInteractionSource,
                        onClick = { onDialogClick?.invoke() }
                    ),
                shape = PgkTheme.shapes.cornersStyle,
                color = PgkTheme.colors.secondaryBackground,
                content = content
            )
        }
    }
}