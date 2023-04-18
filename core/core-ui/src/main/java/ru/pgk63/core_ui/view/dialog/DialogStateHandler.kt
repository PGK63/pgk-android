package ru.pgk63.core_ui.view.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.maxkeppeker.sheets.core.views.BaseTypeState

@Composable
fun StateHandler(
    sheetState: DialogSheetState,
    baseState: BaseTypeState,
) {
    DisposableEffect(sheetState.reset) {
        if (sheetState.reset) {
            baseState.reset()
            sheetState.clearReset()
        }
        onDispose {}
    }
}