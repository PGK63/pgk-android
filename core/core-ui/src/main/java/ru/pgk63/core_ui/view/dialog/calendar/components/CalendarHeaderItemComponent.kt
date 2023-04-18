package ru.pgk63.core_ui.view.dialog.calendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
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

/**
 * The header component within the calendar view.
 * @param date The day of the date that the header represents.
 */
@Composable
internal fun CalendarHeaderItemComponent(date: LocalDate) {
    Row(
        modifier = Modifier
            .aspectRatio(1f, true)
            .padding(4.dp)
            .clip(MaterialTheme.shapes.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = date.format(DateTimeFormatter.ofPattern("E")),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            textAlign = TextAlign.Center
        )
    }
}