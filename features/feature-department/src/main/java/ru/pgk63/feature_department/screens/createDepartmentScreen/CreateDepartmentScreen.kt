package ru.pgk63.feature_department.screens.createDepartmentScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_model.departmentHead.DepartmentHead
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.validation.nameValidation
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.SortingItem
import ru.pgk63.core_ui.view.TextFieldBase
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_department.screens.createDepartmentScreen.viewModel.CreateDepartmentViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun CreateDepartmentRoute(
    viewModel: CreateDepartmentViewModel = hiltViewModel(),
    departmentHeadId: Int?,
    onBackScreen: () -> Unit,
    onDepartmentDetailsScreen: (Int) -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val departmentHeadList = viewModel.responseDepartmentHeadList.collectAsLazyPagingItems()
    var departmentHeadSearchText by remember { mutableStateOf("") }
    var departmentHeadIdSelected by remember { mutableStateOf(departmentHeadId) }

    var createDepartmentResult by remember { mutableStateOf<Result<ru.pgk63.core_model.department.Department>?>(null) }

    viewModel.responseDepartmentCreateResult.onEach {
        createDepartmentResult = it
    }.launchWhenStarted()

    LaunchedEffect(createDepartmentResult) {
        when(createDepartmentResult){
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
                viewModel.responseDepartmentCreateResultToNull()
            }
            is Result.Loading -> Unit
            is Result.Success -> {
                val departmentId = createDepartmentResult!!.data!!.id
                viewModel.responseDepartmentCreateResultToNull()
                onDepartmentDetailsScreen(departmentId)
            }
            null -> Unit
        }
    }

    LaunchedEffect(departmentHeadSearchText){
        if(departmentHeadId == null){
            viewModel.getDepartmentHeadList(search = departmentHeadSearchText.ifEmpty { null })
        }
    }

    CreateDepartmentScreen(
        scaffoldState = scaffoldState,
        departmentHeadId = departmentHeadIdSelected,
        departmentHeadList = departmentHeadList,
        departmentHeadSearchText = departmentHeadSearchText,
        onBackScreen = onBackScreen,
        onDepartmentHeadIdChange = {
            departmentHeadIdSelected = it
        },
        onDepartmentSearchTextChange = {
            departmentHeadSearchText = it
        },
        createDepartment = {
            viewModel.createDepartment(it)
        }
    )
}

@Composable
private fun CreateDepartmentScreen(
    scaffoldState: ScaffoldState,
    departmentHeadId: Int?,
    departmentHeadList: LazyPagingItems<DepartmentHead>,
    departmentHeadSearchText: String,
    onDepartmentSearchTextChange: (String) -> Unit,
    onDepartmentHeadIdChange: (Int) -> Unit,
    onBackScreen: () -> Unit,
    createDepartment: (ru.pgk63.core_model.department.CreateDepartmentBody) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.add_speciality),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
            )
        },
        content = { paddingValues ->
            LazyColumn(contentPadding = paddingValues) {
                item {
                    CreateDepartmentUi(
                        departmentHeadList = departmentHeadList,
                        departmentHeadId = departmentHeadId,
                        departmentHeadSearchText = departmentHeadSearchText,
                        onDepartmentSearchTextChange = onDepartmentSearchTextChange,
                        onDepartmentHeadIdChange = onDepartmentHeadIdChange,
                        createDepartment = createDepartment
                    )
                }
            }
        }
    )
}

@Composable
fun CreateDepartmentUi(
    departmentHeadList: LazyPagingItems<DepartmentHead>,
    departmentHeadId: Int?,
    departmentHeadSearchText: String,
    onDepartmentSearchTextChange: (String) -> Unit,
    onDepartmentHeadIdChange: (Int) -> Unit,
    createDepartment: (ru.pgk63.core_model.department.CreateDepartmentBody) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var name by remember { mutableStateOf("") }

    val nameValidation = nameValidation(name)

    val createDepartmentTextButtonVisible = nameValidation.first && departmentHeadId != null

    Column {
        TextFieldBase(
            text = name,
            onTextChanged = { name = it },
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.CenterHorizontally),
            errorText = if (nameValidation.second != null)
                stringResource(id = nameValidation.second!!) else null,
            hasError = !nameValidation.first,
            label = stringResource(id = R.string.name),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.clearFocus()
            })
        )

        SortingItem(
            title = stringResource(id = ru.pgk63.core_common.R.string.department_head),
            searchText = departmentHeadSearchText,
            content = departmentHeadList,
            onSearchTextChange = onDepartmentSearchTextChange,
            onClickItem = {
                onDepartmentHeadIdChange(it.id)
            },
            selectedItem = {
                departmentHeadId == it.id
            }
        )

        AnimatedVisibility(visible = createDepartmentTextButtonVisible) {
            TextButton(
                onClick = {
                    if(createDepartmentTextButtonVisible) {
                        createDepartment(
                            ru.pgk63.core_model.department.CreateDepartmentBody(
                                name = name,
                                departmentHeadId = departmentHeadId!!
                            )
                        )
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.add),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            }
        }
    }
}
