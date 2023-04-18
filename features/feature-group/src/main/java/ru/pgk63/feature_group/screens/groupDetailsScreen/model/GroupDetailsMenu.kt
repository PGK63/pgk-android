package ru.pgk63.feature_group.screens.groupDetailsScreen.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.vector.ImageVector
import ru.pgk63.core_ui.R

internal enum class GroupDetailsMenu(@StringRes val textId: Int, val icon: ImageVector) {
    ADD_STUDENT(R.string.add_student, Icons.Default.Add),
    ADD_HEADMAN(R.string.add_headman, Icons.Default.Add),
    ADD_DEPUTY_HEADMAN(R.string.add_deputyHeadma, Icons.Default.Add),
    CREATE_JOURNAL(R.string.journal_create, Icons.Default.Add),
    DELETE_GROUP(R.string.delete, Icons.Default.Delete)
}