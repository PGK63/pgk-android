package ru.pgk63.core_ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.PgkTheme

@Composable
fun NextButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(10.dp)
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = text,
            color = PgkTheme.colors.primaryText,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            style = PgkTheme.typography.body
        )

        Card(
            modifier = Modifier.padding(5.dp),
            backgroundColor = PgkTheme.colors.primaryText,
            shape = AbsoluteRoundedCornerShape(90.dp)
        ) {
            Box {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(10.dp),
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = PgkTheme.colors.primaryBackground
                )
            }
        }
    }
}