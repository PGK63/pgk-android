package ru.pgk63.core_ui.view.metaBalls

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.pgk63.core_ui.view.customBlur

@Composable
fun MetaEntity(
    modifier: Modifier = Modifier,
    blur: Float = 30f,
    metaContent: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit = {},
) {

    Box(
        modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.customBlur(blur),
            content = metaContent,
        )
        content()
    }

}