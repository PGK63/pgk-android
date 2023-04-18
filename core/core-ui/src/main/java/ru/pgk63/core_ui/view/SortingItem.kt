package ru.pgk63.core_ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.theme.PgkTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun<T : Any> SortingItem(
    modifier: Modifier = Modifier,
    title:String,
    searchText:String,
    content: LazyPagingItems<T>,
    onSearchTextChange: (String) -> Unit,
    onClickItem: (T) -> Unit = {},
    selectedItem: (T) -> Boolean = { false },
) {
    val scope = rememberCoroutineScope()

    var searchTextFieldVisible by remember { mutableStateOf(false) }
    val searchTextFieldFocusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.heading,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )

            AnimatedVisibility(visible = searchTextFieldVisible) {
                TextFieldSearch(
                    text = searchText,
                    onTextChanged = onSearchTextChange,
                    modifier = Modifier
                        .focusRequester(searchTextFieldFocusRequester),
                    onClose = {
                        searchTextFieldVisible = false
                        onSearchTextChange("")
                    }
                )
            }

            AnimatedVisibility(visible = !searchTextFieldVisible) {
                IconButton(onClick = {
                    scope.launch {
                        searchTextFieldVisible = true
                        delay(100)
                        searchTextFieldFocusRequester.requestFocus()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = PgkTheme.colors.primaryText
                    )
                }
            }
        }

        LazyRow {
            items(content){ item ->
                if(item != null){
                    Card(
                        modifier = Modifier.padding(5.dp),
                        backgroundColor = PgkTheme.colors.secondaryBackground,
                        elevation = 12.dp,
                        shape = PgkTheme.shapes.cornersStyle,
                        border = if(selectedItem.invoke(item))
                            BorderStroke(1.dp, PgkTheme.colors.tintColor)
                        else
                            null,
                        onClick = { onClickItem(item) }
                    ) {
                        Text(
                            text = item.toString(),
                            color = PgkTheme.colors.primaryText,
                            style = PgkTheme.typography.body,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                            modifier = Modifier.padding(5.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun<T : Any> SortingItem(
    modifier: Modifier = Modifier,
    title:String,
    searchText:String,
    gridCells: GridCells,
    content: LazyPagingItems<T>,
    onSearchTextChange: (String) -> Unit,
    onClickItem: (T) -> Unit = {},
    selectedItem: (T) -> Boolean = { false },
) {
    val scope = rememberCoroutineScope()

    var searchTextFieldVisible by remember { mutableStateOf(false) }
    val searchTextFieldFocusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.heading,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )

            AnimatedVisibility(visible = searchTextFieldVisible) {
                TextFieldSearch(
                    text = searchText,
                    onTextChanged = onSearchTextChange,
                    modifier = Modifier
                        .focusRequester(searchTextFieldFocusRequester),
                    onClose = {
                        searchTextFieldVisible = false
                        onSearchTextChange("")
                    }
                )
            }

            AnimatedVisibility(visible = !searchTextFieldVisible) {
                IconButton(onClick = {
                    scope.launch {
                        searchTextFieldVisible = true
                        delay(100)
                        searchTextFieldFocusRequester.requestFocus()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = PgkTheme.colors.primaryText
                    )
                }
            }
        }

        LazyHorizontalGrid(
            rows = gridCells,
            modifier = Modifier.height(176.dp)
        ) {
            items(content){ item ->
                if(item != null){
                    Card(
                        modifier = Modifier.padding(5.dp),
                        backgroundColor = PgkTheme.colors.secondaryBackground,
                        elevation = 12.dp,
                        shape = PgkTheme.shapes.cornersStyle,
                        border = if(selectedItem.invoke(item))
                            BorderStroke(1.dp, PgkTheme.colors.tintColor)
                        else
                            null,
                        onClick = { onClickItem(item) }
                    ) {
                        Text(
                            text = item.toString(),
                            color = PgkTheme.colors.primaryText,
                            style = PgkTheme.typography.body,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                            modifier = Modifier.padding(5.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun<T : Any> SortingItem(
    modifier: Modifier = Modifier,
    title:String,
    searchText:String? = null,
    content: List<T>,
    onSearchTextChange: ((String) -> Unit)? = null,
    onClickItem: (T) -> Unit = {},
    selectedItem: (T) -> Boolean = { false },
) {
    val scope = rememberCoroutineScope()

    var searchTextFieldVisible by remember { mutableStateOf(false) }
    val searchTextFieldFocusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.heading,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )

            AnimatedVisibility(
                visible = searchTextFieldVisible && searchText != null && onSearchTextChange != null
            ) {
                TextFieldSearch(
                    text = searchText!!,
                    onTextChanged = onSearchTextChange!!,
                    modifier = Modifier
                        .focusRequester(searchTextFieldFocusRequester),
                    onClose = {
                        searchTextFieldVisible = false
                        onSearchTextChange("")
                    }
                )
            }

            AnimatedVisibility(
                visible = !searchTextFieldVisible && searchText != null && onSearchTextChange != null
            ) {
                IconButton(onClick = {
                    scope.launch {
                        searchTextFieldVisible = true
                        delay(100)
                        searchTextFieldFocusRequester.requestFocus()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = PgkTheme.colors.primaryText
                    )
                }
            }
        }

        LazyRow {
            items(content){ item ->
                Card(
                    modifier = Modifier.padding(5.dp),
                    backgroundColor = PgkTheme.colors.secondaryBackground,
                    elevation = 12.dp,
                    shape = PgkTheme.shapes.cornersStyle,
                    border = if(selectedItem.invoke(item))
                        BorderStroke(1.dp, PgkTheme.colors.tintColor)
                    else
                        null,
                    onClick = { onClickItem(item) }
                ) {
                    Text(
                        text = item.toString(),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}