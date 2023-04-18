package ru.pgk63.core_ui.view.dialog.calendar.components

import androidx.compose.animation.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarConfig
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarDisplayMode
import ru.pgk63.core_ui.view.dialog.calendar.models.CalendarStyle

/**
 * Top header component of the calendar dialog.
 * @param config The general configuration for the dialog.
 * @param mode The display mode of the dialog.
 * @param navigationDisabled Whenever the navigation of the navigation is disabled.
 * @param prevDisabled Whenever the navigation to the previous period is disabled.
 * @param nextDisabled Whenever the navigation to the next period is disabled.
 * @param cameraDate The current camera-date of the month view.
 * @param onPrev The listener that is invoked when the navigation to the previous period is invoked.
 * @param onNext The listener that is invoked when the navigation to the next period is invoked.
 * @param onMonthClick The listener that is invoked when the month selection was clicked.
 * @param onYearClick The listener that is invoked when the year selection was clicked.
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@ExperimentalMaterial3Api
@Composable
internal fun CalendarTopComponent(
    config: CalendarConfig,
    mode: CalendarDisplayMode,
    navigationDisabled: Boolean,
    prevDisabled: Boolean,
    nextDisabled: Boolean,
    cameraDate: LocalDate,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onMonthClick: () -> Unit,
    onYearClick: () -> Unit,
) {

    val enterTransition = expandIn(expandFrom = Alignment.Center, clip = false) + fadeIn()
    val exitTransition = shrinkOut(shrinkTowards = Alignment.Center, clip = false) + fadeOut()

    val chevronAVD = AnimatedImageVector.animatedVectorResource(R.drawable.avd_chevron_down_up)
    var chevronMonthAtEnd by remember { mutableStateOf(false) }
    var chevronYearAtEnd by remember { mutableStateOf(false) }

    LaunchedEffect(mode) {
        when (mode) {
            CalendarDisplayMode.CALENDAR -> {
                chevronMonthAtEnd = false
                chevronYearAtEnd = false
            }
            CalendarDisplayMode.MONTH -> chevronYearAtEnd = false
            CalendarDisplayMode.YEAR -> chevronMonthAtEnd = false
        }
    }

    val selectableContainerModifier = Modifier.clip(MaterialTheme.shapes.extraSmall)
    val selectableItemModifier = Modifier
        .padding(start = 8.dp)
        .padding(vertical = 4.dp)
        .padding(end = 4.dp)

    Box(modifier = Modifier.fillMaxWidth().background(PgkTheme.colors.secondaryBackground)) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterStart),
            visible = !navigationDisabled && !prevDisabled,
            enter = enterTransition,
            exit = exitTransition
        ) {
            Column(Modifier.align(Alignment.CenterStart)) {
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = PgkTheme.colors.tintColor),
                    modifier = Modifier
                        .size(32.dp),
                    onClick = onPrev
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = config.icons.ChevronLeft,
                        tint = PgkTheme.colors.secondaryBackground,
                        contentDescription = stringResource(
                            when (config.style) {
                                CalendarStyle.MONTH -> R.string.scd_calendar_dialog_prev_month
                                CalendarStyle.WEEK -> R.string.scd_calendar_dialog_prev_week
                            }
                        )
                    )
                }

            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = selectableContainerModifier
                    .clickable(config.monthSelection) {
                        if (config.monthSelection) {
                            chevronMonthAtEnd = !chevronMonthAtEnd
                        }
                        onMonthClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = selectableItemModifier,
                    text = cameraDate.format(DateTimeFormatter.ofPattern("MMM")),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center
                )
                if (config.monthSelection) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = rememberAnimatedVectorPainter(chevronAVD, chevronMonthAtEnd),
                        contentDescription = stringResource(R.string.scd_calendar_dialog_select_month),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row(
                modifier = selectableContainerModifier
                    .clickable(config.yearSelection) {
                        if (config.yearSelection) {
                            chevronYearAtEnd = !chevronYearAtEnd
                        }
                        onYearClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = selectableItemModifier,
                    text = cameraDate.format(DateTimeFormatter.ofPattern("yyyy")),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center
                )
                if (config.yearSelection) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = rememberAnimatedVectorPainter(chevronAVD, chevronYearAtEnd),
                        contentDescription = stringResource(id = R.string.scd_calendar_dialog_select_year),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterEnd),
            visible = !navigationDisabled && !nextDisabled,
            enter = enterTransition,
            exit = exitTransition
        ) {
            Column(Modifier.align(Alignment.CenterEnd)) {
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = PgkTheme.colors.tintColor),
                    modifier = Modifier.size(32.dp),
                    onClick = onNext
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = config.icons.ChevronRight,
                        tint = PgkTheme.colors.secondaryBackground,
                        contentDescription = stringResource(
                            when (config.style) {
                                CalendarStyle.MONTH -> R.string.scd_calendar_dialog_next_month
                                CalendarStyle.WEEK -> R.string.scd_calendar_dialog_next_week
                            }
                        )
                    )
                }
            }
        }
    }
}