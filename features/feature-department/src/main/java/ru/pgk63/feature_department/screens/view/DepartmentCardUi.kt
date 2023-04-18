package ru.pgk63.feature_department.screens.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.ImageCoil

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun DepartmentCardUi(
    department: ru.pgk63.core_model.department.Department,
    onlyDepartmentHead: Boolean = false,
    onClick: () -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        onClick = onClick
    ) {
        Column {

            if(!onlyDepartmentHead){
                Text(
                    text = department.name,
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Divider(color = PgkTheme.colors.secondaryBackground)

            Row(verticalAlignment = Alignment.CenterVertically) {

                if(department.departmentHead.photoUrl != null) {
                    ImageCoil(
                        url = department.departmentHead.photoUrl,
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

                Spacer(modifier = Modifier.width(5.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = department.departmentHead.lastName +
                                " ${department.departmentHead.firstName} " +
                                (department.departmentHead.middleName ?: ""),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        text = stringResource(id = ru.pgk63.core_common.R.string.department_head),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.caption,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
