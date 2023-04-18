package ru.pgk63.core_ui.view.dialog.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.utils.BaseValues
import com.maxkeppeker.sheets.core.views.HeaderComponent
import com.maxkeppeler.sheets.core.R
import ru.pgk63.core_ui.view.dialog.model.DialogHeader

/**
 * Base component for the content structure of a dialog.
 * @param header The content to be displayed inside the dialog that functions as the header view of the dialog.
 * @param contentHorizontalAlignment The horizontal alignment of the layout's children.
 * @param horizontalContentPadding The horizontal padding that is applied to the content.
 * @param content The content to be displayed inside the dialog between the header and the buttons.
 * @param buttonsVisible Display the buttons.
 * @param buttons The content to be displayed inside the dialog that functions as the buttons view of the dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogFrameBase(
    header: DialogHeader? = null,
    contentHorizontalAlignment: Alignment.Horizontal = Alignment.Start,
    horizontalContentPadding: PaddingValues = BaseValues.CONTENT_DEFAULT_PADDING,
    content: @Composable ColumnScope.() -> Unit,
    buttonsVisible: Boolean = true,
    buttons: @Composable (ColumnScope.() -> Unit)? = null,
) {
    val layoutDirection = LocalLayoutDirection.current

    Column(
        modifier = Modifier.wrapContentHeight()
    ) {

        header?.let {
            // Display header
            Column(modifier = Modifier.testTag(TestTags.FRAME_BASE_HEADER)) {
                HeaderComponent(
                    header = header,
                    contentHorizontalPadding = PaddingValues(
                        start = horizontalContentPadding.calculateStartPadding(layoutDirection),
                        end = horizontalContentPadding.calculateEndPadding(layoutDirection),
                    )
                )
            }
        } ?: run {
            // If no header is defined, add extra spacing to the content top padding
            Spacer(
                modifier = Modifier
                    .testTag(TestTags.FRAME_BASE_NO_HEADER)
                    .height(dimensionResource(R.dimen.scd_small_100))
            )
        }

        // Spacing between content and header is usually 16dp
        Column(
            modifier = Modifier
                .testTag(TestTags.FRAME_BASE_CONTENT)
                .padding(
                    PaddingValues(
                        start = horizontalContentPadding.calculateStartPadding(layoutDirection),
                        end = horizontalContentPadding.calculateEndPadding(layoutDirection),
                        // Enforce default top spacing
                        top = dimensionResource(R.dimen.scd_normal_100),
                    )
                )
                .fillMaxWidth(),
            horizontalAlignment = contentHorizontalAlignment,
            content = content
        )

        buttons?.let { buttons ->
            if (buttonsVisible) {
                Column(modifier = Modifier.testTag(TestTags.FRAME_BASE_BUTTONS)) {
                    buttons.invoke(this)
                }
            } else Spacer(
                modifier = Modifier
                    .testTag(TestTags.FRAME_BASE_NO_BUTTONS)
                    .height(dimensionResource(R.dimen.scd_normal_150))
            )
        }
    }
}