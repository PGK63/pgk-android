package com.example.feature_student.screens.studentDetailsScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature_student.screens.studentDetailsScreen.viewModel.StudentDetailsViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.ImageCoil
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun StudentDetailsRoute(
    viewModel: StudentDetailsViewModel = hiltViewModel(),
    studentId: Int,
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit
) {

    var resultStudent by remember { mutableStateOf<Result<ru.pgk63.core_model.student.Student>>(Result.Loading()) }

    viewModel.responseStudents.onEach { result ->
        resultStudent = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getStudentById(id = studentId)
    })

    StudentDetailsScreen(
        resultStudent = resultStudent,
        onBackScreen = onBackScreen,
        onGroupDetailsScreen = onGroupDetailsScreen
    )
}

@Composable
private fun StudentDetailsScreen(
    resultStudent: Result<ru.pgk63.core_model.student.Student>,
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            Column {
                TopBarBack(
                    title = stringResource(id = R.string.student),
                    scrollBehavior = scrollBehavior,
                    onBackClick = onBackScreen
                )
                AnimatedVisibility(
                    visible = resultStudent.data != null,
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    TopBarStudentInfo(
                        student = resultStudent.data!!,
                        onGroupDetailsScreen = onGroupDetailsScreen
                    )
                }
            }
        }
    ) { paddingValues ->
        when(resultStudent) {
            is Result.Error -> ErrorUi()
            is Result.Loading -> LoadingUi()
            is Result.Success -> SuccessUi(
                contentPadding = paddingValues,
                student = resultStudent.data!!
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TopBarStudentInfo(
    student: ru.pgk63.core_model.student.Student,
    onGroupDetailsScreen: (groupId: Int) -> Unit
){

    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = AbsoluteRoundedCornerShape(
            0, 0, 5, 5
        ),
        onClick = { onGroupDetailsScreen(student.group.id) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            if(student.photoUrl != null) {
                ImageCoil(
                    url = student.photoUrl,
                    modifier = Modifier
                        .width((screenWidthDp / 2).dp)
                        .height((screenHeightDp / 4.3).dp)
                )
            }else {
                Image(
                    painter = painterResource(id = R.drawable.profile_photo),
                    contentDescription = null,
                    modifier = Modifier
                        .width((screenWidthDp / 2).dp)
                        .height((screenHeightDp / 4.3).dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = student.fio(),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = student.group.toString(),
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

@Composable
private fun SuccessUi(
    contentPadding: PaddingValues,
    student: ru.pgk63.core_model.student.Student
) {
    LazyColumn(contentPadding = contentPadding) {
        item {
            Spacer(modifier = Modifier.height(15.dp))


        }
    }
}
