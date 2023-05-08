package ru.pgk63.feature_group.screens.createGroupScreen

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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_model.group.CreateGroupBody
import ru.pgk63.core_model.group.CreateGroupResponse
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_group.screens.createGroupScreen.viewModel.CreateGroupViewModel
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.view.SortingItem
import ru.pgk63.core_ui.view.TextFieldBase
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.validation.numberGroupValidation
import ru.pgk63.core_ui.view.NextButton

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun CreateGroupRoute(
    viewModel: CreateGroupViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

    val departmentList = viewModel.responseDepartmentList.collectAsLazyPagingItems()
    val specialityList = viewModel.responseSpecialityList.collectAsLazyPagingItems()
    val teacherList = viewModel.responseTeacherList.collectAsLazyPagingItems()

    var departmentSearchText by remember { mutableStateOf("") }
    var specialitySearchText by remember { mutableStateOf("") }
    var teacherSearchText by remember { mutableStateOf("") }

    var departmentId by remember { mutableStateOf<Int?>(null) }

    var createGroupResult by remember { mutableStateOf<Result<CreateGroupResponse>?>(null) }

    viewModel.responseCreateGroupResult.onEach { result ->
        createGroupResult = result
    }.launchWhenStarted()

    LaunchedEffect(createGroupResult) {
        when(createGroupResult) {
            is Result.Error -> scaffoldState.snackbarHostState.showSnackbar(
                context.getString(R.string.error)
            )
            is Result.Loading -> Unit
            is Result.Success -> {
                val groupId = createGroupResult!!.data!!.id
                viewModel.responseCreateGroupToNull()
                onGroupDetailsScreen(groupId)
            }
            null -> Unit
        }
    }

    LaunchedEffect(departmentId, specialitySearchText) {
        viewModel.getSpecialityList(
            search = specialitySearchText.ifEmpty { null },
            departmentId = departmentId
        )
    }

    LaunchedEffect(departmentSearchText){
        viewModel.getDepartmentList(
            search = departmentSearchText.ifEmpty { null }
        )
    }

    LaunchedEffect(teacherSearchText) {
        viewModel.getTeacherList(
            search = teacherSearchText.ifEmpty { null }
        )
    }

    CreateGroupScreen(
        scaffoldState = scaffoldState,
        departmentList = departmentList,
        specialityList = specialityList,
        teacherList = teacherList,
        departmentSearchText = departmentSearchText,
        specialitySearchText = specialitySearchText,
        teacherSearchText = teacherSearchText,
        onBackScreen = onBackScreen,
        onDepartmentIdChange = {
            departmentId = it
        },
        onDepartmentSearchTextChange = {
            departmentSearchText = it
        },
        onSpecialitySearchTextChange = {
            specialitySearchText = it
        },
        onTeacherSearchTextChange = {
            teacherSearchText = it
        },
        createGroup = {
            viewModel.createGroup(it)
        }
    )
}

@Composable
private fun CreateGroupScreen(
    scaffoldState: ScaffoldState,
    departmentList: LazyPagingItems<ru.pgk63.core_model.department.Department>,
    specialityList: LazyPagingItems<Specialization>,
    teacherList: LazyPagingItems<Teacher>,
    departmentSearchText: String,
    specialitySearchText: String,
    teacherSearchText: String,
    onDepartmentSearchTextChange: (String) -> Unit,
    onSpecialitySearchTextChange: (String) -> Unit,
    onTeacherSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    onDepartmentIdChange: (Int) -> Unit,
    createGroup: (CreateGroupBody) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.group_create),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
            )
        },
        snackbarHost = { state ->
            SnackbarHost(hostState = state) { data ->
                Snackbar(
                    backgroundColor = PgkTheme.colors.secondaryBackground,
                    contentColor = PgkTheme.colors.primaryText,
                    shape = PgkTheme.shapes.cornersStyle,
                    snackbarData = data
                )
            }
        },
        content = { paddingValues ->
            LazyColumn(contentPadding = paddingValues) {
                item {
                    CreateGroupUi(
                        specialityList = specialityList,
                        departmentList = departmentList,
                        teacherList = teacherList,
                        departmentSearchText = departmentSearchText,
                        specialitySearchText = specialitySearchText,
                        teacherSearchText = teacherSearchText,
                        onDepartmentSearchTextChange = onDepartmentSearchTextChange,
                        onSpecialitySearchTextChange = onSpecialitySearchTextChange,
                        onTeacherSearchTextChange = onTeacherSearchTextChange,
                        onDepartmentIdChange = onDepartmentIdChange,
                        createGroup = createGroup
                    )
                }
            }
        }
    )
}

@Composable
private fun CreateGroupUi(
    specialityList: LazyPagingItems<Specialization>,
    departmentList: LazyPagingItems<ru.pgk63.core_model.department.Department>,
    teacherList: LazyPagingItems<Teacher>,
    departmentSearchText: String,
    specialitySearchText: String,
    teacherSearchText: String,
    onDepartmentSearchTextChange: (String) -> Unit,
    onSpecialitySearchTextChange: (String) -> Unit,
    onTeacherSearchTextChange: (String) -> Unit,
    onDepartmentIdChange: (Int) -> Unit,
    createGroup: (CreateGroupBody) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var number by remember { mutableStateOf("") }
    var specialityId by remember { mutableStateOf<Int?>(null) }
    var departmentId by remember { mutableStateOf<Int?>(null) }
    var classroomTeacherId by remember { mutableStateOf<Int?>(null) }

    val numberValidation = numberGroupValidation(number)

    val createGroupTextButtonVisible = numberValidation.first && specialityId != null
            && departmentId != null && classroomTeacherId != null

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
                keyboardType = KeyboardType.Number,
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
                departmentId = it.id
            },
            selectedItem = {
                departmentId == it.id
            }
        )

        SortingItem(
            gridCells = GridCells.Fixed(3),
            title = stringResource(id = R.string.speciality),
            searchText = specialitySearchText,
            content = specialityList,
            onSearchTextChange = onSpecialitySearchTextChange,
            onClickItem = {
                specialityId = it.id
            },
            selectedItem = {
                specialityId == it.id
            }
        )

        SortingItem(
            gridCells = GridCells.Fixed(3),
            title = stringResource(id = R.string.classroomTeacher),
            searchText = teacherSearchText,
            content = teacherList,
            onSearchTextChange = onTeacherSearchTextChange,
            onClickItem = {
                classroomTeacherId = it.id
            },
            selectedItem = {
                classroomTeacherId == it.id
            }
        )

        AnimatedVisibility(visible = createGroupTextButtonVisible) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                NextButton(
                    text = stringResource(id = R.string.register),
                    onClick = {
                        if(createGroupTextButtonVisible) {
                            createGroup(
                                CreateGroupBody(
                                    course = number[0].digitToInt(),
                                    number = number.drop(1),
                                    specialityId = specialityId!!,
                                    departmentId = departmentId!!,
                                    classroomTeacherId = classroomTeacherId!!
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}