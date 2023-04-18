@file:OptIn(ExperimentalFoundationApi::class)

package ru.pgk63.core_ui.paging

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

fun <T: Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    key: ((index: Int) -> Any)? = null,
    itemContent: @Composable (value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount, key = key) { index ->
        itemContent(lazyPagingItems[index])
    }
}

fun <T: Any> LazyStaggeredGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    key: ((index: Int) -> Any)? = null,
    itemContent: @Composable (value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount, key = key) { index ->
        itemContent(lazyPagingItems[index])
    }
}