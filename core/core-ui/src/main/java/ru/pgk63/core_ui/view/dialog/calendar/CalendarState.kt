package ru.pgk63.core_ui.view.dialog.calendar

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.maxkeppeker.sheets.core.views.BaseTypeState
import ru.pgk63.core_ui.view.dialog.calendar.models.*
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarData
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarDisplayMode
import ru.pgk63.core_ui.view.dialog.calendar.utils.*
import ru.pgk63.core_ui.view.dialog.calendar.utils.dateValue
import ru.pgk63.core_ui.view.dialog.calendar.utils.datesValue
import ru.pgk63.core_ui.view.dialog.calendar.utils.initialCameraDate
import ru.pgk63.core_ui.view.dialog.calendar.utils.rangeValue
import ru.pgk63.core_ui.view.dialog.utils.DialogConstants
import java.io.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

/**
 * Handles the calendar state.
 * @param selection The selection configuration for the dialog view.
 * @param config The general configuration for the dialog view.
 * @param stateData The data of the state when the state is required to be restored.
 */
internal class CalendarState(
    val selection: CalendarSelection,
    val config: CalendarConfig,
    stateData: CalendarStateData? = null,
) : BaseTypeState() {

    val today by mutableStateOf(LocalDate.now())
    var mode by mutableStateOf(stateData?.mode ?: CalendarDisplayMode.CALENDAR)
    var cameraDate by mutableStateOf(stateData?.cameraDate ?: selection.initialCameraDate)
    var date = mutableStateOf(stateData?.date ?: selection.dateValue)
    var dates = mutableStateListOf(*(stateData?.dates ?: selection.datesValue))
    var range = mutableStateListOf(*(stateData?.range ?: selection.rangeValue))
    var isRangeSelectionStart by mutableStateOf(stateData?.rangeSelectionStart ?: true)
    var yearsRange by mutableStateOf(getInitYearsRange())
    var monthRange by mutableStateOf(getInitMonthsRange())
    var calendarData by mutableStateOf(getInitCalendarData())
    var valid by mutableStateOf(isValid())

    private fun getInitYearsRange(): IntRange =
        IntRange(config.minYear, config.maxYear.plus(1))

    private fun getInitMonthsRange(): IntRange? {
        val today = LocalDate.now()
        return calcMonthData(config, cameraDate, today)
    }

    private fun getInitCalendarData(): CalendarData {
        return calcCalendarData(config, cameraDate)
    }

    private fun checkValid() {
        valid = isValid()
    }

    private fun refreshData() {
        yearsRange = getInitYearsRange()
        monthRange = getInitMonthsRange()
        calendarData = getInitCalendarData()
    }

    private fun isValid(): Boolean = when (selection) {
        is CalendarSelection.Date -> date.value != null
        is CalendarSelection.Dates -> !dates.isEmpty()
        is CalendarSelection.Period -> range.startValue != null
    }

    val isPrevDisabled: Boolean
        get() {
            val today = LocalDate.now()
            return when (config.style) {
                CalendarStyle.MONTH -> (cameraDate.year <= today.year && cameraDate.monthValue <= today.monthValue)
                        && config.disabledTimeline == CalendarTimeline.PAST
                CalendarStyle.WEEK -> (cameraDate.year <= today.year
                        && cameraDate.weekOfWeekBasedYear <= today.weekOfWeekBasedYear)
                        && config.disabledTimeline == CalendarTimeline.PAST
            }
        }

    val isNextDisabled: Boolean
        get() = when (config.style) {
            CalendarStyle.MONTH -> (cameraDate.year >= today.year
                    && cameraDate.monthValue >= today.monthValue)
                    && config.disabledTimeline == CalendarTimeline.FUTURE
            CalendarStyle.WEEK -> (cameraDate.year >= today.year
                    && cameraDate.weekOfWeekBasedYear >= today.weekOfWeekBasedYear)
                    && config.disabledTimeline == CalendarTimeline.FUTURE
        }

    val cells: Int
        get() = when (mode) {
            CalendarDisplayMode.CALENDAR -> DayOfWeek.values().size
            CalendarDisplayMode.YEAR -> DialogConstants.YEAR_GRID_COLUMNS
            CalendarDisplayMode.MONTH -> DialogConstants.MONTH_GRID_COLUMNS
        }

    val yearIndex: Int
        get() = cameraDate.year.minus(yearsRange.first)

    fun onPrevious() {
        cameraDate = when (config.style) {
            CalendarStyle.MONTH -> cameraDate.minusMonths(1)
            CalendarStyle.WEEK -> cameraDate.previousWeek
        }
        refreshData()
    }

    fun onNext() {
        cameraDate = when (config.style) {
            CalendarStyle.MONTH -> cameraDate.plusMonths(1)
            CalendarStyle.WEEK -> cameraDate.nextWeek
        }
        refreshData()
    }

    fun onMonthSelectionClick() {
        mode = when (mode) {
            CalendarDisplayMode.MONTH -> CalendarDisplayMode.CALENDAR
            CalendarDisplayMode.YEAR -> CalendarDisplayMode.MONTH
            else -> CalendarDisplayMode.MONTH
        }
    }

    fun onYearSelectionClick() {
        mode = when (mode) {
            CalendarDisplayMode.YEAR -> CalendarDisplayMode.CALENDAR
            CalendarDisplayMode.MONTH -> CalendarDisplayMode.YEAR
            else -> CalendarDisplayMode.YEAR
        }
    }

    fun onMonthClick(month: Month) {
        cameraDate = cameraDate.withMonth(month.value).beginOfWeek
        mode = CalendarDisplayMode.CALENDAR
        refreshData()
    }

    fun onYearClick(year: Int) {
        cameraDate = cameraDate.withYear(year).beginOfWeek
        mode = CalendarDisplayMode.CALENDAR
        refreshData()
    }

    fun processSelection(newDate: LocalDate) {
        when (selection) {
            is CalendarSelection.Date -> {
                date.value = newDate
            }
            is CalendarSelection.Dates -> {
                if (dates.contains(newDate)) {
                    dates.remove(newDate)
                } else {
                    dates.add(newDate)
                }
            }
            is CalendarSelection.Period -> {
                val beforeStart =
                    range.startValue?.let { newDate.isBefore(it) } ?: false
                val containsDisabledDate = range.endValue?.let { startDate ->
                    config.disabledDates?.any { it.isAfter(startDate) && it.isBefore(newDate) }
                } ?: false
                if (isRangeSelectionStart || beforeStart || containsDisabledDate) {
                    range[DialogConstants.RANGE_START] = newDate
                    range[DialogConstants.RANGE_END] = null
                    isRangeSelectionStart = false
                } else {
                    range[DialogConstants.RANGE_END] = newDate
                    isRangeSelectionStart = true
                }
            }
        }
        checkValid()
    }

    fun onFinish() {
        when (selection) {
            is CalendarSelection.Date -> selection.onSelectDate(date.value!!)
            is CalendarSelection.Dates -> selection.onSelectDates(dates)
            is CalendarSelection.Period -> selection.onSelectRange(
                range.startValue!!, range.endValue
            )
        }
    }

    override fun reset() {
        date.value = null
        dates.clear()
        range.clear()
    }

    companion object {
        /**
         * [saver] implementation.
         * @param selection The selection configuration for the dialog view.
         * @param config The general configuration for the dialog view.
         */
        fun saver(
            selection: CalendarSelection,
            config: CalendarConfig
        ): Saver<CalendarState, *> = Saver(
            save = { state ->
                CalendarStateData(
                    mode = state.mode,
                    cameraDate = state.cameraDate,
                    date = state.date.value,
                    dates = state.dates.toTypedArray(),
                    range = state.range.toTypedArray(),
                    rangeSelectionStart = state.isRangeSelectionStart
                )
            },
            restore = { data ->
                CalendarState(selection, config, data)
            }
        )
    }


    /**
     * Data class that stores the important information of the current state
     * and can be used by the [saver] to save and restore the state.
     */
    data class CalendarStateData(
        val mode: CalendarDisplayMode,
        val cameraDate: LocalDate,
        val date: LocalDate?,
        val dates: Array<LocalDate>,
        val range: Array<LocalDate?>,
        val rangeSelectionStart: Boolean
    ) : Serializable {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CalendarStateData

            if (mode != other.mode) return false
            if (cameraDate != other.cameraDate) return false
            if (date != other.date) return false
            if (!dates.contentEquals(other.dates)) return false
            if (!range.contentEquals(other.range)) return false
            if (rangeSelectionStart != other.rangeSelectionStart) return false

            return true
        }

        override fun hashCode(): Int {
            var result = mode.hashCode()
            result = 31 * result + cameraDate.hashCode()
            result = 31 * result + (date?.hashCode() ?: 0)
            result = 31 * result + dates.contentHashCode()
            result = 31 * result + range.contentHashCode()
            result = 31 * result + rangeSelectionStart.hashCode()
            return result
        }
    }
}

/**
 * Create a CalendarState and remember it.
 * @param selection The selection configuration for the dialog view.
 * @param config The general configuration for the dialog view.
 */
@Composable
internal fun rememberCalendarState(
    selection: CalendarSelection,
    config: CalendarConfig,
): CalendarState = rememberSaveable(
    inputs = arrayOf(selection, config),
    saver = CalendarState.saver(selection, config),
    init = { CalendarState(selection, config) }
)