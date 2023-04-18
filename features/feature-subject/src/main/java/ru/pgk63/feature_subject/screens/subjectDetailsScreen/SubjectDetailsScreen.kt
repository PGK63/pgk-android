package ru.pgk63.feature_subject.screens.subjectDetailsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.ImageCoil
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_subject.screens.subjectDetailsScreen.viewModel.SubjectDetailsViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun SubjectDetailsRoute(
    viewModel: SubjectDetailsViewModel = hiltViewModel(),
    subjectId: Int,
    onBackScreen: () -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit
) {
    var subjectResult by remember { mutableStateOf<Result<ru.pgk63.core_model.subject.Subject>>(Result.Loading()) }

    val teachers = viewModel.responseTeachers.collectAsLazyPagingItems()

    viewModel.responseSubject.onEach { result ->
        subjectResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getSubjectById(id = subjectId)
        viewModel.getTeachers(subjectId = subjectId)
    })

    SubjectDetailsScreen(
        teachers = teachers,
        subjectResult = subjectResult,
        onBackScreen = onBackScreen,
        onTeacherDetailsScreen = onTeacherDetailsScreen
    )
}

@Composable
private fun SubjectDetailsScreen(
    teachers: LazyPagingItems<Teacher>,
    subjectResult: Result<ru.pgk63.core_model.subject.Subject>,
    onBackScreen: () -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = subjectResult.data?.subjectTitle ?: "",
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        when(subjectResult){
            is Result.Error -> ErrorUi(message = subjectResult.message)
            is Result.Loading -> LoadingUi()
            is Result.Success -> SubjectSuccess(
                teachers = teachers,
                paddingValues = paddingValues,
                onTeacherDetailsScreen = onTeacherDetailsScreen
            )
        }
    }
}

@Composable
private fun SubjectSuccess(
    teachers: LazyPagingItems<Teacher>,
    paddingValues: PaddingValues,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ){
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Column {
                if(teachers.itemCount > 0 ){
                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = stringResource(id = R.string.teachers),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.heading,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }

        items(teachers) { teacher ->
            if(teacher != null){
                TeacherItemUi(
                    teacher = teacher,
                    onClick = { onTeacherDetailsScreen(teacher.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TeacherItemUi(
    teacher: Teacher,
    onClick: () -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Card(
        modifier = Modifier.padding(5.dp),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        shape = PgkTheme.shapes.cornersStyle,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(teacher.photoUrl != null) {
                ImageCoil(
                    url = teacher.photoUrl,
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

            Text(
                text = teacher.fio(),
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
