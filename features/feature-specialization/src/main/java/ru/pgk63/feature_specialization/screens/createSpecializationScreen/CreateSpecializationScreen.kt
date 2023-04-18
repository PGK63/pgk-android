package ru.pgk63.feature_specialization.screens.createSpecializationScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
import ru.pgk63.core_common.api.speciality.model.CreateSpecializationBody
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.validation.nameValidation
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.SortingItem
import ru.pgk63.core_ui.view.TextFieldBase
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_specialization.screens.createSpecializationScreen.viewModel.CreateSpecializationViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun CreateSpecializationRoute(
    viewModel: CreateSpecializationViewModel = hiltViewModel(),
    departmentId:Int?,
    onBackScreen: () -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val departmentList = viewModel.responseDepartmentList.collectAsLazyPagingItems()
    var departmentSearchText by remember { mutableStateOf("") }

    var departmentIdSelected by remember { mutableStateOf(departmentId) }

    var createSpecializationResult by remember { mutableStateOf<Result<Specialization>?>(null) }

    viewModel.responseCreateSpecializationResult.onEach {
        createSpecializationResult = it
    }.launchWhenStarted()

    LaunchedEffect(createSpecializationResult) {
        when(createSpecializationResult){
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
                viewModel.responseCreateSpecializationResultToNull()
            }
            is Result.Loading -> Unit
            is Result.Success -> {
                val specializationId = createSpecializationResult!!.data!!.id
                viewModel.responseCreateSpecializationResultToNull()
                onSpecializationDetailsScreen(specializationId)
            }
            null -> Unit
        }
    }

    LaunchedEffect(departmentSearchText){
        if(departmentId == null){
            viewModel.getDepartmentList(search = departmentSearchText.ifEmpty { null })
        }
    }

    CreateSpecializationScreen(
        scaffoldState = scaffoldState,
        departmentList = departmentList,
        departmentId = departmentIdSelected,
        departmentSearchText = departmentSearchText,
        onDepartmentSearchTextChange = { departmentSearchText = it },
        onBackScreen = onBackScreen,
        onDepartmentIdChange = {
            departmentIdSelected = it
        },
        createSpecialization = {
            viewModel.createSpecialization(it)
        }
    )
}

@Composable
private fun CreateSpecializationScreen(
    scaffoldState: ScaffoldState,
    departmentList: LazyPagingItems<ru.pgk63.core_model.department.Department>,
    departmentId:Int?,
    departmentSearchText: String,
    onDepartmentSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onDepartmentIdChange: (Int) -> Unit,
    createSpecialization: (CreateSpecializationBody) -> Unit
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
                    CreateSpecializationUi(
                        departmentList = departmentList,
                        departmentId = departmentId,
                        departmentSearchText = departmentSearchText,
                        onDepartmentSearchTextChange = onDepartmentSearchTextChange,
                        onDepartmentIdChange = onDepartmentIdChange,
                        createSpecialization = createSpecialization
                    )
                }
            }
        }
    )
}

@Composable
private fun CreateSpecializationUi(
    departmentList: LazyPagingItems<ru.pgk63.core_model.department.Department>,
    departmentId: Int?,
    departmentSearchText: String,
    onDepartmentSearchTextChange: (String) -> Unit,
    onDepartmentIdChange: (Int) -> Unit,
    createSpecialization: (CreateSpecializationBody) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var number by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var nameAbbreviation by remember { mutableStateOf("") }
    var qualification by remember { mutableStateOf("") }

    val numberValidation = nameValidation(number)
    val nameValidation = nameValidation(name)
    val nameAbbreviationValidation = nameValidation(nameAbbreviation)
    val qualificationValidation = nameValidation(qualification)

    val createSpecializationTextButtonVisible = numberValidation.first && nameValidation.first
            && nameAbbreviationValidation.first && qualificationValidation.first && departmentId != null

    Column {

        TextFieldBase(
            text = number,
            onTextChanged = { number = it },
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.CenterHorizontally),
            errorText = if (numberValidation.second != null)
                stringResource(id = numberValidation.second!!) else null,
            hasError = !numberValidation.first,
            label = stringResource(id = R.string.number),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

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
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

        TextFieldBase(
            text = nameAbbreviation,
            onTextChanged = { nameAbbreviation = it },
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.CenterHorizontally),
            errorText = if (nameAbbreviationValidation.second != null)
                stringResource(id = nameAbbreviationValidation.second!!) else null,
            hasError = !nameAbbreviationValidation.first,
            label = stringResource(id = R.string.abbreviation),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

        TextFieldBase(
            text = qualification,
            onTextChanged = { qualification = it },
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.CenterHorizontally),
            errorText = if (qualificationValidation.second != null)
                stringResource(id = qualificationValidation.second!!) else null,
            hasError = !qualificationValidation.first,
            label = stringResource(id = R.string.qualification),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.clearFocus()
            })
        )

        SortingItem(
            gridCells = GridCells.Fixed(3),
            title = stringResource(id = R.string.departmen),
            searchText = departmentSearchText,
            content = departmentList,
            onSearchTextChange = onDepartmentSearchTextChange,
            onClickItem = {
                onDepartmentIdChange(it.id)
            },
            selectedItem = {
                departmentId == it.id
            }
        )

        AnimatedVisibility(visible = createSpecializationTextButtonVisible) {
            TextButton(
                onClick = {
                    if(createSpecializationTextButtonVisible) {
                        createSpecialization(
                            CreateSpecializationBody(
                                number = number,
                                name = name,
                                nameAbbreviation = nameAbbreviation,
                                qualification = qualification,
                                departmentId = departmentId!!
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
