package ru.pgk63.core_ui.view.dialog.calendar.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import ru.pgk63.core_ui.view.dialog.calendar.models.*
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarData
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarDateData
import ru.pgk63.core_ui.view.dialog.calendar.utils.calcCalendarDateData

/**
 * The view that displays all relevant calendar information.
 * @param cells The amount of cells / columns that are used for the calendar grid view.
 * @param config The general configuration for the dialog view.
 * @param selection The selection configuration for the dialog.
 * @param data The calculated data of the current calendar view.
 * @param today The date of today.
 * @param onSelect The listener that is invoked when a date is selected.
 * @param selectedDate The date that is currently selected.
 * @param selectedDates The dates that are currently selected.
 * @param selectedRange The range that is currently selected.
 */
internal fun LazyGridScope.setupCalendarSelectionView(
    cells: Int,
    config: CalendarConfig,
    selection: CalendarSelection,
    data: CalendarData,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
    selectedDate: LocalDate?,
    selectedDates: List<LocalDate>?,
    selectedRange: Pair<LocalDate?, LocalDate?>
) {
    items(DayOfWeek.values()) { CalendarHeaderItemComponent(data.cameraDate.with(it)) }
    item(span = { GridItemSpan(cells) }) { Spacer(modifier = Modifier.height(4.dp)) }
    items(data.offsetStart) {
        CalendarDateItemComponent(
            selection = selection,
            data = CalendarDateData(otherMonth = true)
        )
    }
    items(data.days) { dayIndex ->

        val date = when (config.style) {
            CalendarStyle.MONTH -> data.cameraDate.withDayOfMonth(dayIndex.plus(1))
            CalendarStyle.WEEK -> data.weekCameraDate.plusDays(dayIndex.toLong() + data.offsetStart)
        }

        val dateData = calcCalendarDateData(
            date = date,
            calendarViewData = data,
            today = today,
            selection = selection,
            config = config,
            selectedDate = selectedDate,
            selectedDates = selectedDates,
            selectedRange = selectedRange
        ) ?: return@items

        CalendarDateItemComponent(
            data = dateData,
            selection = selection,
            onDateClick = onSelect
        )
    }
}
