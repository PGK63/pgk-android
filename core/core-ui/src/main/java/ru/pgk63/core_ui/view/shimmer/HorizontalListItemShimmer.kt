package ru.pgk63.core_ui.view.shimmer

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HorizontalListItemShimmer() {
    val brush = rememberAnimationShimmer()
}

@Preview
@Composable
private fun HorizontalListItemShimmerPreview() {
    HorizontalListItemShimmer()
}