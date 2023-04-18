package com.example.feature_guide.screens.teacherDetailsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.paging.compose.items
import com.example.feature_guide.screens.teacherDetailsScreen.viewModel.TeacherDetailsViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_model.subject.Subject
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.ImageCoil
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun TeacherDetailsRoute(
    viewModel: TeacherDetailsViewModel = hiltViewModel(),
    teacherId: Int,
    onBackScreen: () -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onTeacherAddSubjectScreen: (teacherId: Int) -> Unit
) {
    var teacherDetailsResult by remember { mutableStateOf<Result<Teacher>>(Result.Loading()) }

    val subjectList = viewModel.responseSubjectList.collectAsLazyPagingItems()
    val groupList = viewModel.responseGroupList.collectAsLazyPagingItems()

    var user by remember { mutableStateOf(UserLocalDatabase()) }

    viewModel.responseUserLocal.onEach {
        user = it
    }.launchWhenStarted()

    viewModel.responseTeacherDetails.onEach {
        teacherDetailsResult = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getTeacherDetails(teacherId)
        viewModel.getSubjectList(teacherId)
        viewModel.getGroupList(teacherId)
    })

    TeacherDetailsScreen(
        teacherDetailsResult = teacherDetailsResult,
        user = user,
        subjectList = subjectList,
        groupList = groupList,
        onBackScreen = onBackScreen,
        onSubjectDetailsScreen = onSubjectDetailsScreen,
        onGroupDetailsScreen = onGroupDetailsScreen,
        onTeacherAddSubjectScreen = {
            onTeacherAddSubjectScreen(teacherId)
        }
    )
}

@Composable
private fun TeacherDetailsScreen(
    teacherDetailsResult: Result<Teacher>,
    subjectList: LazyPagingItems<Subject>,
    groupList: LazyPagingItems<Group>,
    user: UserLocalDatabase,
    onTeacherAddSubjectScreen: () -> Unit,
    onBackScreen: () -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = teacherDetailsResult.data?.fioAbbreviated()
                    ?: stringResource(id = R.string.teacher),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        when(teacherDetailsResult){
            is Result.Error -> ErrorUi(
                message = teacherDetailsResult.message
                    ?: stringResource(id = R.string.error)
            )
            is Result.Loading -> LoadingUi()
            is Result.Success -> {
                TeacherDetails(
                    contentPadding = paddingValues,
                    user = user,
                    teacher = teacherDetailsResult.data!!,
                    groupList = groupList,
                    subjectList = subjectList,
                    onSubjectDetailsScreen = onSubjectDetailsScreen,
                    onGroupDetailsScreen = onGroupDetailsScreen,
                    onTeacherAddSubjectScreen = onTeacherAddSubjectScreen
                )
            }
        }
    }
}

@Composable
private fun TeacherDetails(
    contentPadding: PaddingValues,
    teacher: Teacher,
    subjectList: LazyPagingItems<Subject>,
    groupList: LazyPagingItems<Group>,
    user: UserLocalDatabase,
    onTeacherAddSubjectScreen: () -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding
    ){
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Column {
                Spacer(modifier = Modifier.height(10.dp))

                TeacherCard(teacher = teacher)

                GroupListUi(
                    groupList = groupList,
                    onGroupDetailsScreen = onGroupDetailsScreen
                )
            }
        }

        subjectListUi(
            subjectList = subjectList,
            teacherId = teacher.id,
            user = user,
            onSubjectDetailsScreen = onSubjectDetailsScreen,
            onTeacherAddSubjectScreen = onTeacherAddSubjectScreen
        )
    }
}

@Composable
private fun TeacherCard(
    teacher: Teacher
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {

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

                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = teacher.fio(),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(id = R.string.teacher),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.caption,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            if(teacher.cabinet != null) {
                Text(
                    text = stringResource(id = R.string.cabinet) +
                            " ${teacher.cabinet}",
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )
            }

            if(teacher.information != null){
                Text(
                    text = teacher.information!!,
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.caption,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun LazyGridScope.subjectListUi(
    subjectList: LazyPagingItems<Subject>,
    teacherId: Int,
    user: UserLocalDatabase,
    onTeacherAddSubjectScreen: () -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit
) {
    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
        Column {
            Spacer(modifier = Modifier.height(25.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.subjects),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.heading,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(start = 20.dp)
                )

                if(
                    user.userRole == UserRole.ADMIN || user.userRole == UserRole.EDUCATIONAL_SECTOR ||
                    (user.userRole == UserRole.TEACHER && teacherId == user.userId)
                ){
                    IconButton(onClick = { onTeacherAddSubjectScreen() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = PgkTheme.colors.tintColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }

    items(subjectList){ subject ->
        subject?.let {
            SubjectListItem(
                subject = subject,
                onClick = { onSubjectDetailsScreen(subject.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SubjectListItem(subject: Subject, onClick: () -> Unit) {

    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        modifier = Modifier.padding(5.dp),
        onClick = onClick
    ) {
        Box {
            Text(
                text = subject.subjectTitle,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun GroupListUi(
    groupList: LazyPagingItems<Group>,
    onGroupDetailsScreen: (groupId: Int) -> Unit
) {
    Column {
        if(groupList.itemCount > 0 ){
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = stringResource(id = R.string.groups),
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.heading,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(start = 20.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))
        }

        LazyRow {
            items(groupList){ group ->
                group?.let {
                    GroupListItem(
                        group = group,
                        onClick = { onGroupDetailsScreen(group.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GroupListItem(group: Group, onClick: () -> Unit) {

    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        modifier = Modifier.padding(5.dp),
        onClick = onClick
    ) {
        Box {
            Text(
                text = group.toString(),
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Center)
            )
        }
    }
}
