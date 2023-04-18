package ru.pgk63.feature_specialization.screens.specializationDetailsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_model.department.Department
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.speciality.model.Specialization
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.paging.items
import ru.pgk63.core_ui.view.ImageCoil
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_specialization.screens.specializationDetailsScreen.viewModel.SpecializationDetailsViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun SpecializationDetailsRoute(
    viewModel: SpecializationDetailsViewModel = hiltViewModel(),
    specializationId: Int,
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit
) {

    var resultSpecialization by remember { mutableStateOf<Result<Specialization>>(Result.Loading()) }

    val groups = viewModel.responseGroup.collectAsLazyPagingItems()
    
    viewModel.responseSpecialization.onEach { result ->
        resultSpecialization = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getById(specializationId)
        viewModel.getGroups(specializationId)
    })

    SpecializationDetailsScreen(
        resultSpecialization = resultSpecialization,
        groups = groups,
        onBackScreen = onBackScreen,
        onGroupDetailsScreen = onGroupDetailsScreen,
        onDepartmentDetailsScreen = onDepartmentDetailsScreen
    )
}

@Composable
private fun SpecializationDetailsScreen(
    resultSpecialization: Result<Specialization>,
    groups: LazyPagingItems<Group>,
    onBackScreen: () -> Unit,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = resultSpecialization.data?.name ?: "",
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        when(resultSpecialization){
            is Result.Error -> ErrorUi(message = resultSpecialization.message)
            is Result.Loading -> LoadingUi()
            is Result.Success -> SpecializationSuccess(
                bottomBarPadding = paddingValues.calculateBottomPadding(),
                specialization = resultSpecialization.data!!,
                groups = groups,
                onGroupDetailsScreen = onGroupDetailsScreen,
                onDepartmentDetailsScreen = onDepartmentDetailsScreen
            )
        }
    }
}

@Composable
private fun SpecializationSuccess(
    specialization: Specialization,
    bottomBarPadding: Dp,
    groups: LazyPagingItems<Group>,
    onGroupDetailsScreen: (groupId: Int) -> Unit,
    onDepartmentDetailsScreen: (departmentId: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
           Column {
               SpecializationDetailsUi(
                   specialization = specialization
               )

               Spacer(modifier = Modifier.height(10.dp))

               DepartmentCard(
                   department = specialization.department,
                   onClick = {
                       onDepartmentDetailsScreen(specialization.department.id)
                   }
               )
           }
        }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Column {
                if(groups.itemCount > 0){
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
            group?.let {
                GroupListItem(
                    group = group,
                    onGroupDetailsScreen = onGroupDetailsScreen
                )
            }
        }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Spacer(modifier = Modifier.height(bottomBarPadding))
        }
    }
}

@Composable
private fun SpecializationDetailsUi(specialization: Specialization) {
    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = specialization.number,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(10.dp)
            )

            Text(
                text = specialization.qualification,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.caption,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DepartmentCard(
    department: Department,
    onClick: () -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        onClick = onClick
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {

                if(department.departmentHead.photoUrl != null) {
                    ImageCoil(
                        url = department.departmentHead.photoUrl,
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
                    text = "${department.departmentHead.lastName} ${department.departmentHead.firstName} " +
                            (department.departmentHead.middleName ?: ""),
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = department.name,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GroupListItem(group: Group, onGroupDetailsScreen: (groupId: Int) -> Unit) {
    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 12.dp,
        shape = PgkTheme.shapes.cornersStyle,
        modifier = Modifier.padding(5.dp),
        onClick = { onGroupDetailsScreen(group.id) }
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
