package ru.pgk63.core_ui.view.table

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.PgkTheme

@Composable
fun Table(
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    columnCount: Int,
    rowCount: Int,
    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit
) {
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }

    Box(modifier = modifier.then(Modifier.horizontalScroll(horizontalScrollState))) {
        LazyColumn(state = verticalLazyListState) {
            items(rowCount) { rowIndex ->
                Column {
                    beforeRow?.invoke(rowIndex)

                    Row(modifier = rowModifier) {
                        (0 until columnCount).forEach { columnIndex ->
                            Box(modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .fillMaxWidth()
                                .layout { measurable, constraints ->
                                    val placeable = measurable.measure(constraints)

                                    val existingWidth = columnWidths[columnIndex] ?: 0
                                    val maxWidth = maxOf(existingWidth, placeable.width)

                                    if (maxWidth > existingWidth) {
                                        columnWidths[columnIndex] = maxWidth
                                    }

                                    layout(width = maxWidth, height = placeable.height) {
                                        placeable.placeRelative(0, 0)
                                    }
                                }) {
                                cellContent(columnIndex, rowIndex)
                            }
                        }
                    }

                    afterRow?.invoke(rowIndex)
                }
            }
        }
    }
}

@Composable
fun TableCell(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = AbsoluteRoundedCornerShape(0.dp),
    textColor:Color = PgkTheme.colors.primaryText,
    borderColor:Color = PgkTheme.colors.primaryText,
    borderDp: Dp = 1.dp,
    onClick: () -> Unit = {}
) {
    Text(
        text = text,
        modifier = modifier
            .border(borderDp, borderColor, shape)
            .padding(8.dp)
            .wrapContentHeight()
            .clickable { onClick() },
        color = textColor,
        textAlign = TextAlign.Center
    )
}