package ru.lfybkf19.feature_journal.view

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import ru.pgk63.core_common.extension.parseToBaseDateFormat
import ru.pgk63.core_ui.view.table.Table
import ru.pgk63.core_ui.view.table.TableCell
import ru.pgk63.core_ui.R

@Composable
internal fun BoxScope.JournalTopicTable(
    modifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    topics: LazyPagingItems<ru.pgk63.core_model.journal.JournalTopic>,
    maxSubjectHours: Int,
    onClickRow: (ru.pgk63.core_model.journal.JournalTopic) -> Unit
) {
    Table(
        modifier = modifier.matchParentSize(),
        rowModifier = Modifier.height(IntrinsicSize.Min),
        verticalLazyListState = verticalLazyListState,
        columnCount = 4,
        rowCount = topics.itemCount + 1,
    ){ columnIndex, rowIndex ->

        if(rowIndex == 0){
            TableCell(
                text = when(columnIndex){
                    0 -> stringResource(id = R.string.date)
                    1 -> stringResource(id = R.string.hours)
                    2 -> stringResource(id = R.string.title)
                    3 -> stringResource(id = R.string.home_work)
                    else -> ""
                },
                modifier = Modifier.fillMaxSize(),
                shape = if(columnIndex == 0 || columnIndex == 3)
                    AbsoluteRoundedCornerShape(
                        topLeft = if(columnIndex == 0) 10.dp else 0.dp,
                        topRight = if(columnIndex == 3) 10.dp else 0.dp,
                        bottomRight = 0.dp,
                        bottomLeft = 0.dp
                    )
                else
                    AbsoluteRoundedCornerShape(0.dp)
            )
        }else if (rowIndex >= 1){
            val topic = topics[rowIndex-1]

            if(topic != null){
                TableCell(
                    text = when(columnIndex){
                        0 -> topic.date.parseToBaseDateFormat()
                        1 -> "${topic.hours}/$maxSubjectHours"
                        2 -> topic.title
                        3 -> topic.homeWork ?: "-"
                        else -> ""
                    },
                    modifier = Modifier.fillMaxSize(),
                    shape = if(rowIndex == topics.itemCount && (columnIndex == 0 || columnIndex == 3))
                        AbsoluteRoundedCornerShape(
                            topLeft = 0.dp,
                            topRight = 0.dp,
                            bottomRight = if(columnIndex == 3) 10.dp else 0.dp,
                            bottomLeft = if(columnIndex == 0) 10.dp else 0.dp
                        )
                    else
                        AbsoluteRoundedCornerShape(0.dp),
                    onClick = {
                        onClickRow(topic)
                    }
                )
            }
        }
    }
}