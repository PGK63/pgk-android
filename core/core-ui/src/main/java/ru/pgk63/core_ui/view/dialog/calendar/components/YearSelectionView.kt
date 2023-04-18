package ru.pgk63.core_ui.view.dialog.calendar.components

import androidx.compose.foundation.lazy.LazyListScope
import java.time.LocalDate

/**
 * The view that displays all relevant year information.
 * @param yearsRange The range of years
 * @param selectedYear The year that is currently selected.
 * @param onYearClick The listener that is invoked when a year is selected.
 */
internal fun LazyListScope.setupYearSelectionView(
    yearsRange: IntRange,
    selectedYear: Int,
    onYearClick: (Int) -> Unit
) {
    items(yearsRange.last.minus(yearsRange.first)) {
        val year = yearsRange.first + it
        val selected = selectedYear == year
        val thisYear = year == LocalDate.now().year
        YearItemComponent(
            year = year,
            thisYear = thisYear,
            selected = selected,
            onYearClick = onYearClick
        )
    }
}