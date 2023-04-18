package ru.pgk63.feature_raportichka.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.departmentHead.DepartmentHead
import ru.pgk63.core_model.deputyHeadma.RaportichkaRow
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_ui.theme.MainTheme
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.view.table.Table
import ru.pgk63.core_ui.view.table.TableCell

@Composable
internal fun BoxScope.RaportichkaTable(
    modifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    rows:List<RaportichkaRow>,
    searchByStudentId: List<Int>? = null,
    searchByGroupsId: List<Int>? = null,
    searchBySubjectsId: List<Int>? = null,
    searchByTeacherId: List<Int>? = null,
    onClickRow: (row: RaportichkaRow) -> Unit = {}
) {
    val sortedRows = rows.sortedBy { it.numberLesson }

    Table(
        modifier = modifier.matchParentSize(),
        rowModifier = Modifier.height(IntrinsicSize.Min),
        verticalLazyListState = verticalLazyListState,
        columnCount = 5,
        rowCount = sortedRows.size + 1,
    ){ columnIndex, rowIndex ->
        if(rowIndex == 0){
            TableCell(
                text = when(columnIndex){
                    0 -> stringResource(id = R.string.para)
                    1 -> stringResource(id = R.string.student)
                    2 -> stringResource(id = R.string.subject)
                    3 -> stringResource(id = R.string.hours)
                    4 -> stringResource(id = R.string.signature)
                    else -> ""
                },
                modifier = Modifier.fillMaxSize(),
                shape = if(columnIndex == 0 || columnIndex == 4)
                    AbsoluteRoundedCornerShape(
                        topLeft = if(columnIndex == 0) 10.dp else 0.dp,
                        topRight = if(columnIndex == 4) 10.dp else 0.dp,
                        bottomRight = 0.dp,
                        bottomLeft = 0.dp
                    )
                else
                    AbsoluteRoundedCornerShape(0.dp)
            )
        }else {
            val row = sortedRows[rowIndex-1]

            val (borderColor, borderDp) = getBorderRaportichkaRow(
                row = row,
                columnIndex = columnIndex,
                searchByStudentId = searchByStudentId,
                searchByGroupsId = searchByGroupsId,
                searchBySubjectsId = searchBySubjectsId,
                searchByTeacherId = searchByTeacherId
            )

            TableCell(
                text = when(columnIndex){
                    0 -> row.numberLesson.toString()
                    1 -> "${row.student.fioAbbreviated()}\n(${row.student.group})"
                    2 -> "${row.subject.subjectTitle}\n(${row.teacher.fioAbbreviated()})"
                    3 -> row.hours.toString()
                    4 -> if(row.confirmation) "✔️" else "❌"
                    else -> ""
                },
                modifier = Modifier.fillMaxSize(),
                borderColor = borderColor,
                borderDp = borderDp,
                shape = if(rowIndex == sortedRows.size && (columnIndex == 0 || columnIndex == 4))
                    AbsoluteRoundedCornerShape(
                        topLeft = 0.dp,
                        topRight = 0.dp,
                        bottomRight = if(columnIndex == 4) 10.dp else 0.dp,
                        bottomLeft = if(columnIndex == 0) 10.dp else 0.dp
                    )
                else
                    AbsoluteRoundedCornerShape(0.dp),
                onClick = {
                    onClickRow(row)
                }
            )
        }
    }
}

@Composable
private fun getBorderRaportichkaRow(
    row: RaportichkaRow,
    columnIndex: Int,
    searchByStudentId: List<Int>? = null,
    searchByGroupsId: List<Int>? = null,
    searchBySubjectsId: List<Int>? = null,
    searchByTeacherId: List<Int>? = null
): Pair<Color, Dp> {

    val tint = Pair(PgkTheme.colors.tintColor, 3.dp)

    return if(searchByStudentId != null && columnIndex == 1 && row.student.id in searchByStudentId){
        tint
    }else if(searchByGroupsId!= null && columnIndex == 1 && row.student.group.id in searchByGroupsId){
        tint
    }else if(searchBySubjectsId != null && columnIndex == 2 && row.subject.id in searchBySubjectsId){
        tint
    }else if(searchByTeacherId != null && columnIndex == 2 && row.teacher.id in searchByTeacherId){
        tint
    }else{
        Pair(PgkTheme.colors.primaryText, 1.dp)
    }
}

@Preview(showBackground = false)
@Composable
private fun RaportichkaTable() {

    val subject = Subject(
        id = 1,
        subjectTitle = "Математика"
    )

    val teacher = Teacher(
        id = 1,
        firstName = "Виктория",
        lastName = "Александровна",
        middleName = "С"
    )

    val department = Department(
        id = 0,
        name = "",
        departmentHead = DepartmentHead(
            id = 0,
            firstName = "",
            lastName = "",
            middleName = null,
            photoUrl = null
        )
    )

    val specialization = Specialization(
        id = 1,
        number = "13.32.31",
        name = "",
        nameAbbreviation = "ИСП",
        qualification = "",
        department = department
    )

    val student = Student(
        id = 1,
        firstName = "Данила",
        lastName = "Беляков",
        middleName = "Се",
        group = Group(
            id = 1,
            course = 2,
            number = 39,
            speciality = specialization,
            department = department,
            classroomTeacher = teacher,
        )
    )

    val rows = listOf(
        RaportichkaRow(
            numberLesson = 1,
            confirmation = true,
            subject = subject,
            teacher = teacher,
            student = student
        ),
        RaportichkaRow(
            numberLesson = 1,
            confirmation = true,
            subject = subject,
            teacher = teacher,
            student = student
        ),
        RaportichkaRow(
            numberLesson = 1,
            confirmation = true,
            subject = subject,
            teacher = teacher,
            student = student
        )
    )

    MainTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = PgkTheme.colors.primaryBackground
        ) {
            Box {
                RaportichkaTable(rows = rows)
            }
        }
    }
}