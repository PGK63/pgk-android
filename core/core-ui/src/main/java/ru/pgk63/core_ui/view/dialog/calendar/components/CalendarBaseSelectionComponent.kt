@file:OptIn(ExperimentalSnapperApi::class)

package ru.pgk63.core_ui.view.dialog.calendar.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarDisplayMode

/**
 * The main component for the selection of the use-case as well as the selection of month and year within the dialog.
 * @param modifier The modifier that is applied to this component.
 * @param yearListState The state of the year list selection view.
 * @param cells The amount of cells / columns that are used for the calendar grid view.
 * @param mode The display mode of the dialog.
 * @param onCalendarView The content that will be displayed if the [CalendarDisplayMode] is in [CalendarDisplayMode.CALENDAR].
 * @param onMonthView The content that will be displayed if the [CalendarDisplayMode] is in [CalendarDisplayMode.MONTH].
 * @param onYearView The content that will be displayed if the [CalendarDisplayMode] is in [CalendarDisplayMode.YEAR].
 */
@OptIn(ExperimentalSnapperApi::class)
@Composable
internal fun CalendarBaseSelectionComponent(
    modifier: Modifier = Modifier,
    yearListState: LazyListState = rememberLazyListState(),
    cells: Int,
    mode: CalendarDisplayMode,
    onCalendarView: LazyGridScope.() -> Unit = {},
    onMonthView: LazyGridScope.() -> Unit = {},
    onYearView: LazyListScope.() -> Unit = {},
) {

    val baseModifier = modifier
        .fillMaxWidth()
        .padding(top = 16.dp)

    val gridYearModifier = baseModifier
        .graphicsLayer { alpha = 0.99F }
        .drawWithContent {
            val colorStops = arrayOf(
                0.0f to Color.Transparent,
                0.25f to Color.Black,
                0.75f to Color.Black,
                1.0f to Color.Transparent
            )
            drawContent()
            drawRect(
                brush = Brush.horizontalGradient(*colorStops),
                blendMode = BlendMode.DstIn
            )
        }

    val behavior = rememberSnapperFlingBehavior(
        lazyListState = yearListState,
        snapOffsetForItem = SnapOffsets.Center,
    )

    Surface(
        color = PgkTheme.colors.secondaryBackground
    ) {
        when (mode) {
            CalendarDisplayMode.CALENDAR -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(cells),
                    modifier = baseModifier,
                    userScrollEnabled = false,
                ) {
                    onCalendarView()
                }
            }
            CalendarDisplayMode.YEAR -> {
                Column(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.scd_calendar_dialog_select_year),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                    )
                    LazyRow(
                        modifier = gridYearModifier,
                        state = yearListState,
                        flingBehavior = behavior,
                        contentPadding = PaddingValues(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        onYearView()
                    }
                }
            }
            CalendarDisplayMode.MONTH -> {
                Column(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.scd_calendar_dialog_select_month),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                    )
                    LazyVerticalGrid(
                        modifier = baseModifier,
                        columns = GridCells.Fixed(cells),
                    ) {
                        onMonthView()
                    }
                }
            }
        }
    }
}