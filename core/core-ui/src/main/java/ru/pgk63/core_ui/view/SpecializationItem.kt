package ru.pgk63.core_ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.PgkTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpecializationItem(
    specialization: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        onClick = onClick
    ) {
        Text(
            text = specialization,
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}