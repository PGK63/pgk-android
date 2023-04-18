package com.example.feature_guide.screens.guideListScreen.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.vector.ImageVector
import ru.pgk63.core_ui.R

internal enum class GuideListMainMenu(@StringRes val textId: Int,val icon: ImageVector) {
    REGISTRATION_DIRECTOR(R.string.add_director, Icons.Default.Add),
    REGISTRATION_DEPARTMENT_HEAD(R.string.add_department_head, Icons.Default.Add),
    REGISTRATION_TEACHER(R.string.add_teacher, Icons.Default.Add),
    REGISTRATION_EDUCATION_SECTOR(R.string.add_education_sector, Icons.Default.Add),
}