package ru.pgk63.core_ui.view.dialog.model

/**
 * An icon from various sources alongside an optional contentDescription and tint.
 * @param text Text used for the button
 * @param icon Icon used for the button, or none if null
 * @param type Style used for the button
 */
data class DialogSelectionButton(
    val text: String,
    val icon: DialogIconSource? = null,
    val type: DialogButtonStyle = DialogButtonStyle.TEXT
)