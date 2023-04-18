package ru.pgk63.feature_raportichka.screens.raportichkaScreen.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import ru.pgk63.core_ui.R

internal enum class RaportichkaRowMenu(@StringRes val textId: Int, val icon: ImageVector) {
    STUDENT_DETAILS(R.string.student, Icons.Default.Person),
    TEACHER_DETAILS(R.string.teacher, Icons.Default.Person),
    SUBJECT_DETAILS(R.string.subject, Icons.Default.Person),
    CONFIRMATION_EDIT(R.string.confirmation_edit, Icons.Default.Edit),
    EDIT(R.string.edit, Icons.Default.Edit),
    DELETE(R.string.delete, Icons.Default.Delete)
}