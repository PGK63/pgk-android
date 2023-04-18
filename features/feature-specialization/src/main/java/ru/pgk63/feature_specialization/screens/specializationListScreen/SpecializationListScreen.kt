package ru.pgk63.feature_specialization.screens.specializationListScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
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
import androidx.paging.compose.items
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.feature_specialization.screens.specializationListScreen.viewModel.SpecializationListViewModel
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.view.*
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.shimmer.VerticalListItemShimmer

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun SpecializationListRoute(
    viewModel: SpecializationListViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onCreateSpecializationScreen: (departmentId: Int?) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    val specializations = viewModel.responseSpecializationList.collectAsLazyPagingItems()
    var user by remember { mutableStateOf(UserLocalDatabase()) }

    viewModel.responseUserLocal.onEach {
        user = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = searchText, block = {
        viewModel.getSpecialization(search = searchText.ifEmpty { null })
    })

    SpecializationListScreen(
        specializations = specializations,
        user = user,
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        onBackScreen = onBackScreen,
        onSpecializationDetailsScreen = onSpecializationDetailsScreen,
        onCreateSpecializationScreen = onCreateSpecializationScreen
    )
}

@Composable
private fun SpecializationListScreen(
    specializations: LazyPagingItems<Specialization>,
    user: UserLocalDatabase,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit,
    onCreateSpecializationScreen: (departmentId: Int?) -> Unit
) {
    val scope = rememberCoroutineScope()

    val searchTextFieldFocusRequester = remember { FocusRequester() }

    val scrollBehavior = rememberToolbarScrollBehavior()
    var searchTextFieldVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.specialties),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
                actions = {
                    AnimatedVisibility(visible = !searchTextFieldVisible) {
                        Row {
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

                            if(
                                user.userRole == UserRole.ADMIN
                                || user.userRole == UserRole.EDUCATIONAL_SECTOR
                            ) {
                                Spacer(modifier = Modifier.height(5.dp))

                                IconButton(onClick = { onCreateSpecializationScreen(null) }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = PgkTheme.colors.primaryText
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
        }
    ) { paddingValues ->

        if (
            specializations.itemCount <= 0 && specializations.loadState.refresh !is LoadState.Loading
        ){
            EmptyUi()
        }else if(specializations.loadState.refresh is LoadState.Error) {
            ErrorUi()
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(specializations) { specialization ->
                    specialization?.let {
                        SpecializationItem(
                            specialization = specialization.name,
                            onClick = { onSpecializationDetailsScreen(specialization.id) }
                        )
                    }
                }

                if (specializations.loadState.append is LoadState.Loading){
                    item {
                        VerticalListItemShimmer()
                    }
                }

                if (
                    specializations.loadState.refresh is LoadState.Loading
                ){
                    items(10) {
                        VerticalListItemShimmer()
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
                }
            }
        }
    }
}
