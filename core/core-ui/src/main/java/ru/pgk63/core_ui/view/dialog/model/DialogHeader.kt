package ru.pgk63.core_ui.view.dialog.model

import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.IconSource

/**
 * Defined implementations of a header for the dialogs.
 */
abstract class DialogHeader {

    /**
     * Standard implementation of a header.
     * @param icon The icon that is displayed above the title..
     * @param title The text that will be set as title.
     */
    data class Default(
        val title: String,
        val icon: IconSource? = null,
    ) : DialogHeader()

    /**
     * Custom implementation of a header.
     */
    data class Custom(
        val header: @Composable () -> Unit
    ) : DialogHeader()
}