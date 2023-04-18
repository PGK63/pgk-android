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
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.view.table.Table
import ru.pgk63.core_ui.view.table.TableCell

@Composable
fun BoxScope.JournalSubjectTable(
    modifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    journalSubjects: List<ru.pgk63.core_model.journal.JournalSubject>,
    onClickRow: (ru.pgk63.core_model.journal.JournalSubject) -> Unit
) {
    Table(
        modifier = modifier.matchParentSize(),
        rowModifier = Modifier.height(IntrinsicSize.Min),
        verticalLazyListState = verticalLazyListState,
        columnCount = 3,
        rowCount = journalSubjects.size + 1,
    ){ columnIndex, rowIndex ->
        if(rowIndex == 0){
            TableCell(
                text = when(columnIndex){
                    0 -> stringResource(id = R.string.subject)
                    1 -> stringResource(id = R.string.teacher)
                    2 -> stringResource(id = R.string.hours)
                    else -> ""
                },
                modifier = Modifier.fillMaxSize(),
                shape = if(columnIndex == 0 || columnIndex == 2)
                    AbsoluteRoundedCornerShape(
                        topLeft = if(columnIndex == 0) 10.dp else 0.dp,
                        topRight = if(columnIndex == 2) 10.dp else 0.dp,
                        bottomRight = 0.dp,
                        bottomLeft = 0.dp
                    )
                else
                    AbsoluteRoundedCornerShape(0.dp)
            )
        }else {
            val journalSubject = journalSubjects[rowIndex-1]

            TableCell(
                text = when(columnIndex){
                    0 -> journalSubject.subject.toString()
                    1 -> journalSubject.teacher.fioAbbreviated()
                    2 -> journalSubject.hours.toString()
                    else -> ""
                },
                modifier = Modifier.fillMaxSize(),
                shape = if(rowIndex == journalSubjects.size && (columnIndex == 0 || columnIndex == 2))
                    AbsoluteRoundedCornerShape(
                        topLeft = 0.dp,
                        topRight = 0.dp,
                        bottomRight = if(columnIndex == 2) 10.dp else 0.dp,
                        bottomLeft = if(columnIndex == 0) 10.dp else 0.dp
                    )
                else
                    AbsoluteRoundedCornerShape(0.dp),
                onClick = {
                    onClickRow(journalSubject)
                }
            )
        }
    }
}