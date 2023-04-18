package ru.pgk63.feature_raportichka.screens.raportichkaScreen.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.vector.ImageVector
import ru.pgk63.core_ui.R

internal enum class AddRaportichkaMenu(@StringRes val textId: Int, val icon: ImageVector) {
    ADD_RAPORTICHKA(R.string.raportichka_add, Icons.Default.Add),
    RAPORTICHKA_ADD_ROW(R.string.raportichka_row_add, Icons.Default.Add)
}