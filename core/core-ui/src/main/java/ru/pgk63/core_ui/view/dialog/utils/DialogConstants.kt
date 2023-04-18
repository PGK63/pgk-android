package ru.pgk63.core_ui.view.dialog.utils

import com.maxkeppeker.sheets.core.icons.LibIcons
import java.time.LocalDate

internal object DialogConstants {
    // Default values for CalendarConfig class.

    internal const val DEFAULT_MIN_YEAR = 1900
    internal val DEFAULT_MAX_YEAR = LocalDate.now().year
    internal const val DEFAULT_MONTH_SELECTION = false
    internal const val DEFAULT_YEAR_SELECTION = false

    // Constants for various indices for better readability

    internal const val RANGE_START = 0
    internal const val RANGE_END = 1
    internal const val FIRST_DAY_IN_MONTH = 1
    internal const val DAYS_IN_WEEK = 7

    // Misc

    internal const val YEAR_GRID_COLUMNS = 1
    internal const val MONTH_GRID_COLUMNS = 4

    // Behaviours

    const val SUCCESS_DISMISS_DELAY = 600L

    val DEFAULT_ICON_STYLE = LibIcons.Rounded
}