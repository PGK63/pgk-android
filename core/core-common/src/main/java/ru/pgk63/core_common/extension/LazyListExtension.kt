package ru.pgk63.core_common.extension

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.*

@Composable
fun LazyGridState.isFirstItemVisible(): Boolean {
    val visible by remember {
        derivedStateOf {
            this.firstVisibleItemIndex == 0
        }
    }

    return visible
}