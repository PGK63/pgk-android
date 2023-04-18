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
import com.maxkeppeler.sheets.core.R as RC

/**
 * The item component of the year selection view.
 * @param year The year that this item represents.
 * @param thisYear The current year.
 * @param selected If the year is selected.
 * @param onYearClick The listener that is invoked when a year is selected.
 */
@Composable
internal fun YearItemComponent(
    year: Int,
    thisYear: Boolean,
    selected: Boolean,
    onYearClick: (Int) -> Unit
) {
    val textStyle =
        when {
            selected -> MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.onPrimary)
            thisYear -> MaterialTheme.typography.titleSmall.copy(MaterialTheme.colorScheme.primary)
            else -> MaterialTheme.typography.bodyMedium
        }

    val baseModifier = Modifier
        .wrapContentWidth()
        .clip(MaterialTheme.shapes.small)
        .clickable { onYearClick(year) }

    val selectedModifier = baseModifier
        .background(MaterialTheme.colorScheme.primary)
        .clickable { onYearClick(year) }

    Column(
        modifier = if (selected) selectedModifier else baseModifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = dimensionResource(RC.dimen.scd_small_150))
                .padding(vertical = dimensionResource(RC.dimen.scd_small_100)),
            text = year.toString(),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            textAlign = TextAlign.Center
        )
    }
}