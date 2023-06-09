package ru.pgk63.core_ui.view.dialog.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import ru.pgk63.core_ui.theme.PgkTheme
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import com.maxkeppeler.sheets.core.R as RC

/**
 * The item component of the month selection view.
 * @param month The month that this item represents.
 * @param thisMonth The current month.
 * @param selected If the month is selected.
 * @param onMonthClick The listener that is invoked when a year is selected.
 */
@Composable
internal fun MonthItemComponent(
    month: Month? = null,
    thisMonth: Boolean = false,
    selected: Boolean = false,
    onMonthClick: ((Month) -> Unit)? = null
) {
    val textStyle =
        when {
            selected -> MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.onPrimary)
            thisMonth -> MaterialTheme.typography.titleSmall.copy(MaterialTheme.colorScheme.primary)
            else -> MaterialTheme.typography.bodyMedium
        }

    val baseModifier = Modifier
        .wrapContentWidth()
        .padding(dimensionResource(RC.dimen.scd_small_50))

    val normalModifier = baseModifier
        .clip(MaterialTheme.shapes.small)
        .clickable { onMonthClick?.invoke(month!!) }

    val selectedModifier = normalModifier
        .background(MaterialTheme.colorScheme.primary)

    Column(
        modifier = when {
            month == null -> baseModifier
            selected -> selectedModifier
            else -> normalModifier
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = dimensionResource(RC.dimen.scd_small_150))
                .padding(vertical = dimensionResource(RC.dimen.scd_small_100)),
            text = month?.let {
                LocalDate.now().withMonth(month.value)
                    .format(DateTimeFormatter.ofPattern("MMM"))
            } ?: "",
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}