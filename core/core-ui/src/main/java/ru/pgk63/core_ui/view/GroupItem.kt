package ru.pgk63.core_ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.PgkTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupItem(
    group: String,
    classroomTeacher: String,
    onClick: () -> Unit
) {
    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        modifier = Modifier.padding(5.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = group,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp)
            )

            Text(
                text = classroomTeacher,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.caption,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}