package ru.pgk63.core_ui.view.dialog.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.views.IconComponent
import ru.pgk63.core_ui.view.dialog.model.DialogHeader
import com.maxkeppeler.sheets.core.R

/**
 * Header component of the dialog.
 * @param header Implementation of the header.
 */
@ExperimentalMaterial3Api
@Composable
fun HeaderComponent(
    header: DialogHeader,
    contentHorizontalPadding: PaddingValues,
) {
    when (header) {
        is DialogHeader.Custom -> header.header.invoke()
        is DialogHeader.Default -> DefaultHeaderComponent(header, contentHorizontalPadding)
    }
}

/**
 * The default header component for a dialog.
 * @param header The data of the default header.
 */
@Composable
private fun DefaultHeaderComponent(
    header: DialogHeader.Default,
    contentHorizontalPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .testTag(TestTags.HEADER_DEFAULT)
            .fillMaxWidth()
            .padding(contentHorizontalPadding)
            .padding(top = dimensionResource(id = R.dimen.scd_normal_150)),
        horizontalAlignment = if (header.icon != null) Alignment.CenterHorizontally else Alignment.Start
    ) {
        header.icon?.let {
            IconComponent(
                modifier = Modifier
                    .testTag(TestTags.HEADER_DEFAULT_ICON)
                    .size(dimensionResource(R.dimen.scd_size_150)),
                iconSource = it,
                defaultTint = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            text = header.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .testTag(TestTags.HEADER_DEFAULT_TEXT)
                .padding(
                    top = if (header.icon != null) dimensionResource(id = R.dimen.scd_normal_100)
                    else 0.dp
                ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = if (header.icon != null) TextAlign.Center else TextAlign.Start
        )
    }
}