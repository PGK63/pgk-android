@file:OptIn(ExperimentalMaterial3Api::class)

package ru.pgk63.core_ui.view.dialog.calendar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import ru.pgk63.core_ui.view.dialog.DialogBase
import ru.pgk63.core_ui.view.dialog.DialogSheetState
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarConfig
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarSelection
import ru.pgk63.core_ui.view.dialog.model.DialogHeader

/**
 * Calendar dialog for the use-case to select a date or period in a typical calendar-view.
 * @param state The state of the sheet.
 * @param config The general configuration for the dialog.
 * @param header The header to be displayed at the top of the dialog.
 * @param properties DialogProperties for further customization of this dialog's behavior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    state: DialogSheetState,
    selection: CalendarSelection,
    config: CalendarConfig = CalendarConfig(),
    header: DialogHeader? = null,
    properties: DialogProperties = DialogProperties(),
) {

    DialogBase(
        state = state,
        properties = properties,
    ) {
        CalendarView(
            sheetState = state,
            config = config,
            header = header,
            selection = selection,
        )
    }
}