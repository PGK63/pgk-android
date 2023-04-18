package ru.pgk63.feature_department.screens.departmentDetailsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_department.screens.departmentDetailsScreen.viewModel.DepartmentDetailsViewModel
import ru.pgk63.feature_department.screens.view.DepartmentCardUi

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun DepartmentDetailsRoute(
    viewModel: DepartmentDetailsViewModel = hiltViewModel(),
    departmentId: Int,
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit
) {
    var departmentResult by remember { mutableStateOf<Result<Department>>(Result.Loading()) }

    val specializations = viewModel.responseSpecializationList.collectAsLazyPagingItems()

    val groups = viewModel.responseGroup.collectAsLazyPagingItems()

    viewModel.responseDepartment.onEach { result ->
        departmentResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getDepartmentById(departmentId)
        viewModel.getSpecialization(departmentId)
        viewModel.getGroups(departmentId = departmentId)
    })

    DepartmentDetailsScreen(
        departmentResult = departmentResult,
        specializations = specializations,
        groups = groups,
        onBackScreen = onBackScreen,
        onGroupDetailsScreen = onGroupDetailsScreen,
        onSpecializationDetailsScreen = onSpecializationDetailsScreen
    )
}

@Composable
private fun DepartmentDetailsScreen(
    departmentResult: Result<Department>,
    specializations: LazyPagingItems<Specialization>,
    groups: LazyPagingItems<Group>,
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = departmentResult.data?.name ?: "",
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        when(departmentResult){
            is Result.Error -> ErrorUi(message = departmentResult.message)
            is Result.Loading -> LoadingUi()
            is Result.Success -> DepartmentSuccess(
                paddingValues = paddingValues,
                department = departmentResult.data!!,
                specializations = specializations,
                groups = groups,
                onGroupDetailsScreen = onGroupDetailsScreen,
                onSpecializationDetailsScreen = onSpecializationDetailsScreen
            )
        }
    }
}

@Composable
private fun DepartmentSuccess(
    paddingValues: PaddingValues,
    department: Department,
    specializations: LazyPagingItems<Specialization>,
    groups: LazyPagingItems<Group>,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onSpecializationDetailsScreen: (specializationId: Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(paddingValues),
        columns = GridCells.Fixed(2)
    ){
        item(
            span = { GridItemSpan(maxCurrentLineSpan) }
        ) {
            DepartmentCardUi(
                department = department,
                onlyDepartmentHead = true,
                onClick = {}
            )
        }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Specializations(
                specializations = specializations,
                onClickItem = onSpecializationDetailsScreen
            )
        }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Column {
                if(specializations.itemCount > 0 ){
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
            }
        }

        items(groups){ group ->
            if (group != null) {
                GroupListItem(
                    group = group,
                    onClick = { onGroupDetailsScreen(group.id) }
                )
            }
        }
    }
}

@Composable
private fun Specializations(
    specializations: LazyPagingItems<Specialization>,
    onClickItem: (specializationId: Int) -> Unit
) {
    Column {
        if(specializations.itemCount > 0 ){
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = stringResource(id = R.string.specialties),
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.heading,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(start = 20.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))
        }

        LazyRow {
            items(specializations){ specialization ->
                specialization?.let {
                    SpecializationListItem(
                        specialization = specialization,
                        onClick = { onClickItem(specialization.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SpecializationListItem(
    specialization: Specialization,
    onClick: () -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    Card(
        modifier = Modifier
            .padding(5.dp)
            .width(screenWidthDp / 2)
            .height(screenWidthDp / 3),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = specialization.nameAbbreviation,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = specialization.qualification,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.caption,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GroupListItem(
    group: Group,
    onClick: () -> Unit
) {
    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        modifier = Modifier.padding(5.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "${group.speciality.nameAbbreviation}-${group.course}${group.number}",
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp)
            )

            Text(
                text = "${group.classroomTeacher.lastName} " +
                        "${group.classroomTeacher.firstName[0]}" +
                        ".${group.classroomTeacher.middleName?.getOrNull(0) ?: ""}",
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.caption,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}