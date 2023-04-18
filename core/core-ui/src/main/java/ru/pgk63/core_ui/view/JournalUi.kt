package ru.pgk63.core_ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.R

@Composable
fun JournalUi(
    modifier: Modifier = Modifier,
    group: String,
    course: String,
    semester: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .widthIn(min = 100.dp)
            .heightIn(min = 150.dp)
            .clip(PgkTheme.shapes.cornersStyle)
            .background(PgkTheme.colors.journalColor)
            .clickable { onClick() }
    ) {
        Card(
            backgroundColor = PgkTheme.colors.primaryText,
            shape = PgkTheme.shapes.cornersStyle,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = group,
                color = PgkTheme.colors.secondaryBackground,
                textAlign = TextAlign.Center,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier
                    .widthIn(min = 70.dp)
                    .padding(2.dp)
            )
        }

        Card(
            backgroundColor = PgkTheme.colors.primaryText,
            shape = PgkTheme.shapes.cornersStyle,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(3.dp)
            ) {
                Text(
                    text = "${stringResource(id = R.string.course)} $course",
                    color = PgkTheme.colors.secondaryBackground,
                    textAlign = TextAlign.Center,
                    style = PgkTheme.typography.caption,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                )

                Text(
                    text = "${stringResource(id = R.string.semester)} $semester",
                    color = PgkTheme.colors.secondaryBackground,
                    textAlign = TextAlign.Center,
                    style = PgkTheme.typography.caption,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                )
            }
        }
    }
}