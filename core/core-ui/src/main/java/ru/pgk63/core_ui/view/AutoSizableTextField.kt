package ru.pgk63.core_ui.view

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import ru.pgk63.core_ui.theme.PgkTheme
import kotlin.math.ceil

@Composable
fun AutoSizableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 32.sp,
    maxLines: Int = Int.MAX_VALUE,
    minFontSize: TextUnit,
    scaleFactor: Float = 0.9f,
    label: String? = null
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        var nFontSize = fontSize

        val calculateParagraph = @Composable {
            Paragraph(
                text = value,
                style = TextStyle(fontSize = nFontSize),
                constraints = Constraints(
                    maxWidth = with(LocalDensity.current) { ceil(maxWidth.toPx()).toInt() }
                ),
                density = LocalDensity.current,
                fontFamilyResolver = LocalFontFamilyResolver.current,
                maxLines = maxLines
            )
        }

        var intrinsics = calculateParagraph()
        with(LocalDensity.current) {
            while ((intrinsics.height.toDp() > maxHeight || intrinsics.didExceedMaxLines) && nFontSize >= minFontSize) {
                nFontSize *= scaleFactor
                intrinsics = calculateParagraph()
            }
        }

        TextFieldBase(
            text = value,
            onTextChanged = onValueChange,
            modifierTextField = Modifier.fillMaxSize(),
            shape = PgkTheme.shapes.cornersStyle,
            singleLine = false,
            maxLines = maxLines,
            textStyle = TextStyle(fontSize = nFontSize),
            label = label,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
        )
    }
}