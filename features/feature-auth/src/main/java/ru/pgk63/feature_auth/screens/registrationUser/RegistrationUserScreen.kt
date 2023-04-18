package ru.pgk63.feature_auth.screens.registrationUser

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_model.headman.HeadmanRegistrationBody
import ru.pgk63.core_model.student.Student
import ru.pgk63.core_model.student.StudentRegistrationBody
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.api.user.model.UserRegistrationResponse
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.validation.nameValidation
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.BaseLottieAnimation
import ru.pgk63.core_ui.view.LottieAnimationType
import ru.pgk63.core_ui.view.TextFieldBase
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_auth.screens.registrationUser.model.RegistrationUserState
import ru.pgk63.feature_auth.screens.registrationUser.viewModel.RegistrationUserViewModel
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_ui.paging.items

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun RegistrationUserRoute(
    viewModel: RegistrationUserViewModel = hiltViewModel(),
    userRole: UserRole,
    groupId: Int?,
    onBackScreen: () -> Unit
) {
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()

    val students = viewModel.responseStudents.collectAsLazyPagingItems()
    var resultRegistration by remember { mutableStateOf<Result<UserRegistrationResponse>?>(null) }

    viewModel.responseResultRegistration.onEach { result ->
        resultRegistration = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = resultRegistration, block = {
        when(resultRegistration) {
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
            }
            is Result.Loading -> Unit
            is Result.Success -> Unit
            null -> Unit
        }
    })

    RegistrationUserScreen(
        scaffoldState = scaffoldState,
        userRole = userRole,
        groupId = groupId,
        resultRegistration = resultRegistration,
        onBackScreen = onBackScreen,
        registration = { state ->
            viewModel.registration(state)
        },
        getStudents = {

            LaunchedEffect(key1 = Unit, block = {
                viewModel.getStudents(groupId = groupId)
            })

            students
        }
    )
}

@Composable
private fun RegistrationUserScreen(
    scaffoldState: ScaffoldState,
    userRole: UserRole,
    groupId: Int?,
    resultRegistration: Result<UserRegistrationResponse>?,
    onBackScreen: () -> Unit,
    registration: (RegistrationUserState) -> Unit,
    getStudents: @Composable () -> LazyPagingItems<Student>
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.registration),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
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
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AnimatedVisibility(visible = resultRegistration is Result.Success) {
                    RegistrationSuccess(
                        userRegistrationResponse = resultRegistration?.data!!
                    )
                }

                AnimatedVisibility(visible = resultRegistration !is Result.Success) {
                    RegistrationUi(
                        userRole = userRole,
                        resultRegistration = resultRegistration,
                        groupId = groupId,
                        registration = registration,
                        getStudents = getStudents
                    )
                }
            }
        }
    }
}

@Composable
private fun RegistrationSuccess(
    userRegistrationResponse: UserRegistrationResponse
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BaseLottieAnimation(
            type = LottieAnimationType.REGISTRATION,
            modifier = Modifier
                .padding(5.dp)
                .width((screenWidthDp / 1.5).dp)
                .height((screenHeightDp / 2).dp)
        )

        Text(
            text = "${stringResource(id = R.string.success)}!\n" +
                    "\n${stringResource(id = R.string.your_password)}" +
                    "\n${userRegistrationResponse.passowrd}",
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.heading,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(15.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RegistrationUi(
    userRole: UserRole,
    resultRegistration: Result<UserRegistrationResponse>?,
    groupId: Int?,
    registration: (RegistrationUserState) -> Unit,
    getStudents: @Composable () -> LazyPagingItems<Student>
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val focusManager = LocalFocusManager.current

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var middleName by rememberSaveable { mutableStateOf("") }
    var studentIdSelected by rememberSaveable { mutableStateOf<Int?>(null) }

    val firstNameValidation = nameValidation(firstName)
    val lastNameValidation = nameValidation(lastName)

    val registrationTextButtonVisible = when(userRole) {
        UserRole.STUDENT -> firstName.isNotEmpty() && lastName.isNotEmpty()  && groupId !=null
        UserRole.HEADMAN, UserRole.DEPUTY_HEADMAN -> studentIdSelected != null
        else -> firstName.isNotEmpty() && lastName.isNotEmpty() && middleName.isNotEmpty()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BaseLottieAnimation(
            type = LottieAnimationType.REGISTRATION,
            modifier = Modifier
                .padding(5.dp)
                .width((screenWidthDp / 1.5).dp)
                .height((screenHeightDp / 2).dp)
        )

        if(resultRegistration is Result.Loading){
            CircularProgressIndicator(
                color = PgkTheme.colors.tintColor,
                modifier = Modifier.padding(5.dp)
            )
        }else if(resultRegistration is Result.Error){
            Text(
                text =  stringResource(id = R.string.error),
                color = PgkTheme.colors.errorColor,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                style = PgkTheme.typography.body,
                modifier = Modifier.padding(5.dp)
            )
        }

        if(userRole != UserRole.HEADMAN && userRole != UserRole.DEPUTY_HEADMAN) {
            TextFieldBase(
                text = firstName,
                onTextChanged = { firstName = it },
                maxChar = 256,
                label = stringResource(id = R.string.firstName),
                modifier = Modifier.padding(5.dp),
                errorText = if(firstNameValidation.second != null)
                    stringResource(id = firstNameValidation.second!!) else null,
                hasError = !firstNameValidation.first,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                })
            )

            TextFieldBase(
                text = lastName,
                onTextChanged = { lastName = it },
                maxChar = 256,
                label = stringResource(id = R.string.lastName),
                modifier = Modifier.padding(5.dp),
                errorText = if (lastNameValidation.second != null)
                    stringResource(id = lastNameValidation.second!!) else null,
                hasError = !lastNameValidation.first,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                })
            )

            TextFieldBase(
                text = middleName,
                onTextChanged = { middleName = it },
                maxChar = 256,
                label = stringResource(id = R.string.middleName),
                modifier = Modifier.padding(5.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )
        }

        if(userRole == UserRole.HEADMAN || userRole == UserRole.DEPUTY_HEADMAN){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = stringResource(id = R.string.students),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.heading,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(15.dp),
                    textAlign = TextAlign.Center
                )

                StudentsListUi(
                    studentIdSelected = studentIdSelected,
                    getStudents = getStudents,
                    onStudentIdSelectedChange = { studentIdSelected = it }
                )
            }
        }

        AnimatedVisibility(visible = registrationTextButtonVisible) {
            TextButton(
                onClick = {
                    if(registrationTextButtonVisible) {
                        registration(userRole.toState(
                            firstName = firstName,
                            lastName = lastName,
                            middleName = middleName.ifEmpty { null },
                            groupId = groupId,
                            studentId = studentIdSelected
                        ))
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.register),
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

@Composable
private fun StudentsListUi(
    studentIdSelected: Int?,
    onStudentIdSelectedChange: (Int) -> Unit,
    getStudents: @Composable () -> LazyPagingItems<Student>
) {
    val students = getStudents()

    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        modifier = Modifier.height(176.dp)
    ) {
        items(students) { student ->
            if(student != null) {
                StudentItemUi(
                    student = student,
                    selected = studentIdSelected == student.id,
                    onClick = {
                        onStudentIdSelectedChange(student.id)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun StudentItemUi(
    student: Student,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        modifier = Modifier.padding(5.dp),
        border = if(selected) BorderStroke(
            width = 1.dp,
            color = PgkTheme.colors.tintColor
        ) else null,
        onClick = onClick
    ) {
        Text(
            text = student.fioAbbreviated(),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(5.dp)
        )
    }
}

private fun UserRole.toState(
    firstName:String,
    lastName:String,
    middleName: String?,
    studentId: Int?,
    groupId: Int?
): RegistrationUserState {

    return when(this) {
        UserRole.STUDENT -> {
            RegistrationUserState.Student(
                body = StudentRegistrationBody(
                    firstName = firstName,
                    lastName = lastName,
                    middleName = middleName,
                    groupId = groupId!!
                )
            )
        }
        UserRole.HEADMAN -> {
            RegistrationUserState.Headman(
                deputy = false,
                body = HeadmanRegistrationBody(studentId = studentId!!)
            )
        }
        UserRole.DEPUTY_HEADMAN -> {
            RegistrationUserState.Headman(
                deputy = true,
                body = HeadmanRegistrationBody(studentId = studentId!!)
            )
        }
        else -> RegistrationUserState.Base(
            userRole = this,
            body = UserRegistrationBody(
                firstName = firstName,
                lastName = lastName,
                middleName = middleName
            )
        )
    }
}