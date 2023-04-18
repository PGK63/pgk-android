
package ru.pgk63.feature_group.screens.groupListScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.view.*
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.shimmer.VerticalListItemShimmer
import ru.pgk63.feature_group.screens.groupListScreen.viewModel.GroupListViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun GroupListRoute(
    viewModel: GroupListViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onCreateGroupScreen: () -> Unit
) {
    val groups = viewModel.responseGroup.collectAsLazyPagingItems()

    var searchText by remember { mutableStateOf("") }
    var user by remember { mutableStateOf(UserLocalDatabase()) }

    viewModel.responseUserLocal.onEach {
        user = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = searchText, block = {
        viewModel.getGroups(
            search = searchText.ifEmpty { null }
        )
    })

    GroupListScreen(
        groups = groups,
        user = user,
        searchText = searchText,
        onBackScreen = onBackScreen,
        onGroupDetailsScreen = onGroupDetailsScreen,
        onCreateGroupScreen = onCreateGroupScreen,
        onSearchTextChange = { searchText = it }
    )
}

@Composable
private fun GroupListScreen(
    groups: LazyPagingItems<Group>,
    user: UserLocalDatabase,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onCreateGroupScreen: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = rememberToolbarScrollBehavior()

    val searchTextFieldFocusRequester = remember { FocusRequester() }
    var searchTextFieldVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.groups),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
                actions = {
                    AnimatedVisibility(visible = !searchTextFieldVisible) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
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

                            if(user.userRole == UserRole.TEACHER || user.userRole == UserRole.ADMIN) {
                                Spacer(modifier = Modifier.width(5.dp))

                                IconButton(onClick = { onCreateGroupScreen() }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = PgkTheme.colors.tintColor
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(visible = searchTextFieldVisible) {
                        TextFieldSearch(
                            text = searchText,
                            onTextChanged = onSearchTextChange,
                            modifier = Modifier.focusRequester(searchTextFieldFocusRequester),
                            onClose = {
                                searchTextFieldVisible = false
                                onSearchTextChange("")
                            }
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            if (
                groups.itemCount <= 0 && groups.loadState.refresh !is LoadState.Loading
            ){
                EmptyUi()
            }else if(groups.loadState.refresh is LoadState.Error) {
                ErrorUi()
            }else{
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ){
                    items(groups){ group ->
                        if(group != null) {
                            GroupItem(
                                group = group.toString(),
                                classroomTeacher = group.classroomTeacher.fioAbbreviated(),
                                onClick = { onGroupDetailsScreen(group.id) }
                            )
                        }
                    }

                    if (groups.loadState.append is LoadState.Loading){
                        item {
                            VerticalListItemShimmer()
                        }
                    }

                    if (
                        groups.loadState.refresh is LoadState.Loading
                    ){
                        items(20) {
                            VerticalListItemShimmer()
                        }
                    }

                    item(
                        span = { GridItemSpan(maxCurrentLineSpan) }
                    ) {
                        Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
                    }
                }
            }
        }
    )
}
