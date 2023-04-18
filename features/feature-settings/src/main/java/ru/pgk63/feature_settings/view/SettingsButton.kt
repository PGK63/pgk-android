package ru.pgk63.feature_settings.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.PgkTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SettingsButton(
    title: String,
    body: String? = null,
    @DrawableRes iconId: Int? = null,
    switchCheck: Boolean? = null,
    textColor: Color = PgkTheme.colors.primaryText,
    onSwitchCheckedChange: ((Boolean) -> Unit)? = null,
    rowContent: (@Composable RowScope.() -> Unit)? = null,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        backgroundColor = PgkTheme.colors.primaryBackground
    ) {
        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if(iconId != null){
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(25.dp),
                        tint = textColor
                    )
                }

                Text(
                    text = title,
                    color = textColor,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 5.dp,
                        bottom = 5.dp
                    )
                )

                if(rowContent != null){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        rowContent.invoke(this)
                    }
                }

                if(switchCheck != null && onSwitchCheckedChange != null){
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Switch(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            checked = switchCheck,
                            onCheckedChange = onSwitchCheckedChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PgkTheme.colors.tintColor,
                                checkedTrackColor = PgkTheme.colors.tintColor,
                                uncheckedThumbColor = PgkTheme.colors.secondaryBackground,
                                uncheckedTrackColor = PgkTheme.colors.errorColor
                            )
                        )
                    }
                }
            }

            if(body != null){
                Text(
                    text = body,
                    color = textColor,
                    style = PgkTheme.typography.caption,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(5.dp),
                    fontWeight = FontWeight.W100
                )
            }

            Divider(color = PgkTheme.colors.secondaryBackground, modifier = Modifier.padding(5.dp))
        }
    }
}