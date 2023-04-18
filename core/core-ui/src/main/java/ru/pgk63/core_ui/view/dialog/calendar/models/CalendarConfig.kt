package ru.pgk63.core_ui.view.dialog.calendar.models

import com.maxkeppeker.sheets.core.icons.LibIcons
import com.maxkeppeker.sheets.core.models.base.BaseConfigs
import ru.pgk63.core_ui.view.dialog.utils.DialogConstants
import ru.pgk63.core_ui.view.dialog.utils.DialogConstants.DEFAULT_ICON_STYLE
import java.time.LocalDate

/**
 * The general configuration for the calendar dialog.
 * @param style The style of the calendar.
 * @param monthSelection Allow the direct selection of a month.
 * @param yearSelection Allow the direct selection of a year.
 * @param minYear The minimum year that is selectable.
 * @param maxYear The maximum year that is selectable.
 * @param disabledDates A list of dates that will be marked as disabled and can not be selected.
 * @param disabledTimeline The timeline you want to disable and which dates can not be selected.
 * @param icons The style of icons that are used for dialog/ view-specific icons.
 */
class CalendarConfig(
    val style: CalendarStyle = CalendarStyle.MONTH,
    val monthSelection: Boolean = DialogConstants.DEFAULT_MONTH_SELECTION,
    val yearSelection: Boolean = DialogConstants.DEFAULT_YEAR_SELECTION,
    val minYear: Int = DialogConstants.DEFAULT_MIN_YEAR,
    val maxYear: Int = DialogConstants.DEFAULT_MAX_YEAR,
    val disabledDates: List<LocalDate>? = null,
    val disabledTimeline: CalendarTimeline? = null,
    override val icons: LibIcons = DEFAULT_ICON_STYLE,
) : BaseConfigs()