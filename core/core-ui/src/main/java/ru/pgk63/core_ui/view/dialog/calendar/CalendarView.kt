package ru.pgk63.core_ui.view.dialog.calendar

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import ru.pgk63.core_ui.view.dialog.DialogSheetState
import ru.pgk63.core_ui.view.dialog.StateHandler
import ru.pgk63.core_ui.view.dialog.calendar.components.*
import ru.pgk63.core_ui.view.dialog.calendar.components.CalendarBaseSelectionComponent
import ru.pgk63.core_ui.view.dialog.calendar.components.CalendarTopComponent
import ru.pgk63.core_ui.view.dialog.calendar.components.setupCalendarSelectionView
import ru.pgk63.core_ui.view.dialog.calendar.components.setupMonthSelectionView
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarConfig
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarDisplayMode
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarSelection
import ru.pgk63.core_ui.view.dialog.calendar.utils.endValue
import ru.pgk63.core_ui.view.dialog.calendar.utils.startValue
import ru.pgk63.core_ui.view.dialog.components.DialogButtonsComponent
import ru.pgk63.core_ui.view.dialog.components.DialogFrameBase
import ru.pgk63.core_ui.view.dialog.model.DialogBaseBehaviors
import ru.pgk63.core_ui.view.dialog.model.DialogHeader
import java.time.LocalDate

/**
 * Calendar dialog for the use-case to select a date or period in a typical calendar-view.
 * @param sheetState The state of the sheet.
 * @param selection The selection configuration for the dialog view.
 * @param config The general configuration for the dialog view.
 * @param header The header to be displayed at the top of the dialog view.
 */
@ExperimentalMaterial3Api
@Composable
fun CalendarView(
    sheetState: DialogSheetState,
    selection: CalendarSelection,
    config: CalendarConfig = CalendarConfig(),
    header: DialogHeader? = null,
) {
    val calendarState =
        rememberCalendarState(selection, config)
    StateHandler(sheetState, calendarState)

    val coroutine = rememberCoroutineScope()
    val onSelection: (LocalDate) -> Unit = {
        calendarState.processSelection(it)
        DialogBaseBehaviors.autoFinish(
            selection = selection,
            coroutine = coroutine,
            onSelection = calendarState::onFinish,
            onFinished = sheetState::finish,
            onDisableInput = calendarState::disableInput
        )
    }

    val yearListState = rememberLazyListState()
    LaunchedEffect(calendarState.mode) {
        if (calendarState.mode == CalendarDisplayMode.YEAR) {
            yearListState.scrollToItem(calendarState.yearIndex)
        }
    }

    DialogFrameBase(
        header = header,
        content = {
            CalendarTopComponent(
                config = config,
                mode = calendarState.mode,
                navigationDisabled = calendarState.monthRange == null || calendarState.mode != CalendarDisplayMode.CALENDAR,
                prevDisabled = calendarState.isPrevDisabled,
                nextDisabled = calendarState.isNextDisabled,
                cameraDate = calendarState.cameraDate,
                onPrev = calendarState::onPrevious,
                onNext = calendarState::onNext,
                onMonthClick = { calendarState.onMonthSelectionClick() },
                onYearClick = { calendarState.onYearSelectionClick() },
            )
            CalendarBaseSelectionComponent(
                modifier = Modifier.wrapContentHeight(),
                yearListState = yearListState,
                mode = calendarState.mode,
                cells = calendarState.cells,
                onCalendarView = {
                    setupCalendarSelectionView(
                        config = config,
                        cells = calendarState.cells,
                        data = calendarState.calendarData,
                        today = calendarState.today,
                        selection = selection,
                        onSelect = onSelection,
                        selectedDate = calendarState.date.value,
                        selectedDates = calendarState.dates,
                        selectedRange = Pair(
                            calendarState.range.startValue,
                            calendarState.range.endValue
                        ),
                    )
                },
                onMonthView = {
                    setupMonthSelectionView(
                        monthRange = calendarState.monthRange!!,
                        selectedMonth = calendarState.cameraDate.month,
                        onMonthClick = calendarState::onMonthClick
                    )
                },
                onYearView = {
                    setupYearSelectionView(
                        yearsRange = calendarState.yearsRange,
                        selectedYear = calendarState.cameraDate.year,
                        onYearClick = calendarState::onYearClick
                    )
                }
            )
        },
        buttonsVisible = selection.withButtonView && calendarState.mode == CalendarDisplayMode.CALENDAR
    ) {
        DialogButtonsComponent(
            selection = selection,
            onPositiveValid = calendarState.valid,
            onNegative = { selection.onNegativeClick?.invoke() },
            onPositive = calendarState::onFinish,
            onClose = sheetState::finish
        )
    }
}