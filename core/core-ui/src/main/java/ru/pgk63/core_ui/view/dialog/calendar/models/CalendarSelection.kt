package ru.pgk63.core_ui.view.dialog.calendar.models

import android.util.Range
import ru.pgk63.core_ui.view.dialog.model.DialogBaseSelection
import ru.pgk63.core_ui.view.dialog.model.DialogSelectionButton
import java.time.LocalDate

/**
 * The selection configuration for the calendar dialog.
 */
sealed class CalendarSelection : DialogBaseSelection() {

    /**
     * Select a date.
     * @param withButtonView Show the dialog with the buttons view.
     * @param extraButton An extra button that can be used for a custom action.
     * @param onExtraButtonClick The listener that is invoked when the extra button is clicked.
     * @param negativeButton The button that will be used as a negative button.
     * @param onNegativeClick The listener that is invoked when the negative button is clicked.
     * @param positiveButton The button that will be used as a positive button.
     * @param selectedDate The date that is selected by default.
     * @param onSelectDate The listener that returns the selected date.
     */
    class Date(
        override val withButtonView: Boolean = true,
        override val extraButton: DialogSelectionButton? = null,
        override val onExtraButtonClick: (() -> Unit)? = null,
        override val negativeButton: DialogSelectionButton? = null,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButton: DialogSelectionButton? = null,
        val selectedDate: LocalDate? = null,
        val onSelectDate: (date: LocalDate) -> Unit
    ) : CalendarSelection()

    /**
     * Select multiple dates.
     * @param extraButton An extra button that can be used for a custom action.
     * @param onExtraButtonClick The listener that is invoked when the extra button is clicked.
     * @param negativeButton The button that will be used as a negative button.
     * @param onNegativeClick The listener that is invoked when the negative button is clicked.
     * @param positiveButton The button that will be used as a positive button.
     * @param selectedDates A list of dates that is selected by default.
     * @param onSelectDates The listener that returns the selected dates.
     */
    class Dates(
        override val extraButton: DialogSelectionButton? = null,
        override val onExtraButtonClick: (() -> Unit)? = null,
        override val negativeButton: DialogSelectionButton? = null,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButton: DialogSelectionButton? = null,
        val selectedDates: List<LocalDate>? = null,
        val onSelectDates: (dates: List<LocalDate>) -> Unit
    ) : CalendarSelection()

    /**
     * Select a range (start and end date).
     * @param withButtonView Show the dialog with the buttons view.
     * @param extraButton An extra button that can be used for a custom action.
     * @param onExtraButtonClick The listener that is invoked when the extra button is clicked.
     * @param negativeButton The button that will be used as a negative button.
     * @param onNegativeClick The listener that is invoked when the negative button is clicked.
     * @param positiveButton The button that will be used as a positive button.
     * @param selectedRange The range that is selected by default.
     * @param onSelectRange The listener that returns the selected range.
     */
    class Period(
        override val withButtonView: Boolean = true,
        override val extraButton: DialogSelectionButton? = null,
        override val onExtraButtonClick: (() -> Unit)? = null,
        override val negativeButton: DialogSelectionButton? = null,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButton: DialogSelectionButton? = null,
        val selectedRange: Range<LocalDate>? = null,
        val onSelectRange: (startDate: LocalDate, endDate: LocalDate?) -> Unit
    ) : CalendarSelection()
}