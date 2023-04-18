package ru.pgk63.core_ui.view.dialog.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.PgkTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarDateData
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarSelection

/**
 * The date item component of the calendar view.
 * @param modifier The modifier that is applied to this component.
 * @param data The data for the date.
 * @param selection The selection configuration for the dialog view.
 * @param onDateClick The listener that is invoked when a date is clicked.
 */
@Composable
internal fun CalendarDateItemComponent(
    modifier: Modifier = Modifier,
    data: CalendarDateData,
    selection: CalendarSelection,
    onDateClick: (LocalDate) -> Unit = {},
) {
    val today = data.date == LocalDate.now()
    val shape = when {
        data.selectedStart -> MaterialTheme.shapes.medium.copy(
            topEnd = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        )
        data.selectedEnd -> MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        )
        data.selectedBetween -> RoundedCornerShape(0)
        else -> PgkTheme.shapes.cornersStyle
    }
    val color = when {
        data.selectedBetween -> PgkTheme.colors.tintColor
        else -> PgkTheme.colors.tintColor
    }

    val disabledModifier = Modifier
        .aspectRatio(1f, true)
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.surfaceVariant)

    val selectedModifier = Modifier
        .aspectRatio(1f, true)
        .clip(shape)
        .background(color)
        .clickable { data.date?.let { onDateClick(it) } }

    val normalModifier = Modifier
        .aspectRatio(1f, true)
        .clip(MaterialTheme.shapes.medium)
        .clickable { data.date?.let { onDateClick(it) } }

    val otherMonthModifier = Modifier
        .aspectRatio(1f, true)

    val textStyle =
        when {
            data.disabled -> PgkTheme.typography.body
            data.selectedBetween || data.selected -> PgkTheme.typography.body
            today -> PgkTheme.typography.body.copy(PgkTheme.colors.errorColor)
            else -> PgkTheme.typography.body
        }

    val parentModifier = when (selection) {
        is CalendarSelection.Date -> modifier.padding(2.dp)
        is CalendarSelection.Dates -> modifier.padding(2.dp)
        is CalendarSelection.Period -> modifier.padding(vertical = 2.dp)
    }

    val cellModifier = when {
        data.otherMonth -> otherMonthModifier
        data.disabled -> disabledModifier
        data.selected -> selectedModifier
        else -> normalModifier
    }

    Column(modifier = parentModifier) {
        Row(
            modifier = cellModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = data.date?.format(DateTimeFormatter.ofPattern("d"))
                    ?.takeUnless { data.otherMonth } ?: "",
                color = if(today) PgkTheme.colors.errorColor else PgkTheme.colors.primaryText,
                style = textStyle,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                textAlign = TextAlign.Center
            )
        }
    }
}