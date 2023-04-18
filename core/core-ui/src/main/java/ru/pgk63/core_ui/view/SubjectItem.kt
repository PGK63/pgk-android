package ru.pgk63.core_ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.PgkTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubjectItem(
    subjectTitle: String,
    onClick: () -> Unit
) {
    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        modifier = Modifier.padding(5.dp),
        onClick = onClick
    ) {
        Box {
            Text(
                text = subjectTitle,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Center)
            )
        }
    }
}