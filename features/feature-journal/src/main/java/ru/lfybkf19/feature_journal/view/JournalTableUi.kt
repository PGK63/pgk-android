package ru.lfybkf19.feature_journal.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import ru.pgk63.core_common.extension.parseToBaseDateFormat
import ru.pgk63.core_ui.view.table.Table
import ru.pgk63.core_ui.view.table.TableCell
import ru.pgk63.core_ui.R
import java.util.*

@Composable
internal fun BoxScope.JournalTableUi(
    modifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    rows: List<ru.pgk63.core_model.journal.JournalRow>,
    students: LazyPagingItems<ru.pgk63.core_model.student.Student>,
    addColumnButtonVisibility: Boolean,
    onClickStudent: (ru.pgk63.core_model.student.Student) -> Unit,
    onClickEvaluation: (
        ru.pgk63.core_model.journal.JournalEvaluation?,
        columnId: Int?,
        rowId: Int?,
        student: ru.pgk63.core_model.student.Student,
        date: Date?
    ) -> Unit
) {
    val columns = rows.map { it.columns }.flatten()
    val dates = columns.map { it.date }.distinct().sortedBy { it }

    Table(
        modifier = modifier.matchParentSize(),
        rowModifier = Modifier.height(IntrinsicSize.Min),
        verticalLazyListState = verticalLazyListState,
        columnCount = dates.size + 2,
        rowCount = students.itemCount + 1,
    ){ columnIndex, rowIndex ->

        if(rowIndex == 0 && columnIndex == 0){
            TableCell(
                text = stringResource(id = R.string.student),
                modifier = Modifier.fillMaxSize()
            )
        }

        if(columnIndex == 0 && rowIndex != 0){
            val student = students[rowIndex-1]

            TableCell(
                text = student?.fioAbbreviated() ?: "",
                modifier = Modifier.fillMaxSize(),
                onClick = { student?.let { onClickStudent(it) } }
            )
        }

        if(columnIndex != 0 && rowIndex == 0 && columnIndex != dates.size + 1){
            val date = dates[columnIndex-1]

            TableCell(
                text = date.parseToBaseDateFormat(),
                modifier = Modifier.fillMaxSize()
            )
        }

        if(columnIndex != 0 && rowIndex != 0 && columnIndex != dates.size + 1){
            val date = dates[columnIndex-1]
            val student = students[rowIndex-1]

            val row = rows.firstOrNull { it.student.id == student?.id }

            val column =  row?.columns?.firstOrNull { it.date == date }

            if(column != null && student != null){
                TableCell(
                    text = column.evaluation.text,
                    modifier = Modifier.fillMaxSize(),
                    onClick = {  onClickEvaluation(
                        column.evaluation,
                        column.id,
                        row.id,
                        student,
                        date
                    )}
                )
            }else if (student != null) {
                TableCell(
                    text = "-",
                    modifier = Modifier.fillMaxSize(),
                    onClick = {  onClickEvaluation(
                        column?.evaluation,
                        column?.id,
                        row?.id,
                        student,
                        date
                    )}
                )
            }
        }

        if (addColumnButtonVisibility) {
            if(rowIndex != 0 && columnIndex == dates.size + 1) {

                val student = students[rowIndex-1]

                if(student != null){
                    TableCell(
                        text = "+",
                        modifier = Modifier.fillMaxSize(),
                        onClick = {
                            onClickEvaluation(
                                null, null,
                                null, student, null
                            )
                        }
                    )
                }
            }
        }
    }
}