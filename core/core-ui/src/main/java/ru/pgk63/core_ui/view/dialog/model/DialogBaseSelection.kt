package ru.pgk63.core_ui.view.dialog.model

/**
 * Base selection for dialog-specific selections.
 */
abstract class DialogBaseSelection {
    open val withButtonView: Boolean = true
    open val extraButton: DialogSelectionButton? = null
    open val onExtraButtonClick: (() -> Unit)? = null
    open val negativeButton: DialogSelectionButton? = null
    open val onNegativeClick: (() -> Unit)? = null
    open val positiveButton: DialogSelectionButton? = null
}