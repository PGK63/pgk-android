package ru.pgk63.core_ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GuideItemUi(
    name:String,
    post:String,
    photoUrl: String?,
    onClick: () -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Card(
        modifier = Modifier.padding(5.dp),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        shape = PgkTheme.shapes.cornersStyle,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(photoUrl != null) {
                ImageCoil(
                    url = photoUrl,
                    modifier = Modifier
                        .width((screenWidthDp / 2).dp)
                        .height((screenHeightDp / 4.3).dp)
                )
            }else {
                Image(
                    painter = painterResource(id = R.drawable.profile_photo),
                    contentDescription = null,
                    modifier = Modifier
                        .width((screenWidthDp / 2).dp)
                        .height((screenHeightDp / 4.3).dp)
                )
            }

            Text(
                text = name,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = post,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.caption,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}