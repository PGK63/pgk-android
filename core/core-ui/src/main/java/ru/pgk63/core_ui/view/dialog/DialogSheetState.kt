package ru.pgk63.core_ui.view.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.io.Serializable

/**
 * Handles the base behavior of any use-case view and the dialog, if dialog is used.
 * @param visible If the dialog is initially visible.
 * @param embedded If the view is embedded (in a Dialog, PopUp, BottomSheet or another container that has its own state).
 * @param onCloseRequest The listener that is invoked when the dialog was closed through any cause.
 * @param onFinishedRequest The listener that is invoked when the dialog's use-case was finished by the user accordingly (negative, positive, selection).
 * @param onDismissRequest The listener that is invoked when the dialog was dismissed.
 */
class DialogSheetState(
    visible: Boolean = false,
    embedded: Boolean = true,
    val onFinishedRequest: (DialogSheetState.() -> Unit)? = null,
    val onDismissRequest: (DialogSheetState.() -> Unit)? = null,
    val onCloseRequest: (DialogSheetState.() -> Unit)? = null,
) {
    var visible by mutableStateOf(visible)
    var embedded by mutableStateOf(embedded)
    var reset by mutableStateOf(false)

    /**
     * Display the dialog / view.
     */
    fun show() {
        visible = true
    }

    /**
     * Hide the dialog / view.
     */
    fun hide() {
        visible = false
        onDismissRequest?.invoke(this)
        onCloseRequest?.invoke(this)
    }

    internal fun clearReset() {
        reset = false
    }

    /**
     * Reset the current state data.
     */
    fun invokeReset() {
        reset = true
    }

    // Closed
    internal fun dismiss() {
        if (!embedded) visible = false
        onDismissRequest?.invoke(this)
        onCloseRequest?.invoke(this)
    }

    /**
     * Finish the use-case view.
     */
    fun finish() {
        /*
            We don't want to remove the view itself,
            but inform through the state that the use-case is done.
            The parent container (Dialog, PopUp, BottomSheet)
            can be hidden with the use-case view.
         */
        if (!embedded) visible = false
        onFinishedRequest?.invoke(this)
        onCloseRequest?.invoke(this)
    }

    internal fun markAsEmbedded() {
        embedded = false
    }

    companion object {

        fun Saver(): Saver<DialogSheetState, *> = Saver(
            save = { state ->
                SheetStateData(
                    visible = state.visible,
                    embedded = state.embedded,
                    onCloseRequest = state.onCloseRequest,
                    onFinishedRequest = state.onFinishedRequest,
                    onDismissRequest = state.onDismissRequest
                )
            },
            restore = { data ->
                DialogSheetState(
                    visible = data.visible,
                    embedded = data.embedded,
                    onCloseRequest = data.onCloseRequest,
                    onFinishedRequest = data.onFinishedRequest,
                    onDismissRequest = data.onDismissRequest,
                )
            }
        )
    }

    /**
     * Data class that stores the important information of the current state
     * and can be used by the [Saver] to save and restore the state.
     */
    data class SheetStateData(
        val visible: Boolean,
        val embedded: Boolean,
        val onCloseRequest: (DialogSheetState.() -> Unit)?,
        val onFinishedRequest: (DialogSheetState.() -> Unit)?,
        val onDismissRequest: (DialogSheetState.() -> Unit)?,
    ) : Serializable
}

/**
 * Create a SheetState and remember it.
 * @param visible The initial visibility.
 * @param embedded if the use-case is embedded in a container (dialog, bottomSheet, popup, ...)
 * @param onCloseRequest The listener that is invoked when the dialog was closed through any cause.
 * @param onFinishedRequest The listener that is invoked when the dialog's use-case was finished by the user accordingly (negative, positive, selection).
 * @param onDismissRequest The listener that is invoked when the dialog was dismissed.
 */
@Composable
fun rememberSheetState(
    visible: Boolean = false,
    embedded: Boolean = true,
    onCloseRequest: (DialogSheetState.() -> Unit)? = null,
    onFinishedRequest: (DialogSheetState.() -> Unit)? = null,
    onDismissRequest: (DialogSheetState.() -> Unit)? = null,
): DialogSheetState =
    rememberSaveable(
        saver = DialogSheetState.Saver(),
        init = {
            DialogSheetState(
                visible = visible,
                embedded = embedded,
                onCloseRequest = onCloseRequest,
                onFinishedRequest = onFinishedRequest,
                onDismissRequest = onDismissRequest
            )
        }
    )