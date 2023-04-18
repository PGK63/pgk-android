package com.example.feature_guide.screens.directorDetailsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
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
import com.example.feature_guide.screens.directorDetailsScreen.viewModel.DirectorViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.api.director.model.Director
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.ImageCoil
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun DirectorDetailsRoute(
    viewModel: DirectorViewModel = hiltViewModel(),
    directorId: Int,
    onBackScreen: () -> Unit
) {
    var directorDetailsResult by remember { mutableStateOf<Result<Director>>(Result.Loading()) }

    viewModel.responseDirectorDetails.onEach { result ->
        directorDetailsResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getDirectorDetails(directorId)
    })

    DirectorDetailsScreen(
        directorDetailsResult = directorDetailsResult,
        onBackScreen = onBackScreen
    )
}

@Composable
private fun DirectorDetailsScreen(
    directorDetailsResult: Result<Director>,
    onBackScreen: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = directorDetailsResult.data?.fioAbbreviated()
                    ?: stringResource(id = ru.pgk63.core_common.R.string.director),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        }
    ) { paddingValues ->
        when(directorDetailsResult){
            is Result.Error -> ErrorUi()
            is Result.Loading -> LoadingUi()
            is Result.Success -> {
                LazyColumn(contentPadding = paddingValues) {
                    item {
                        DirectorCard(
                            director = directorDetailsResult.data!!
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DirectorCard(
    director: Director
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

                if(director.photoUrl != null) {
                    ImageCoil(
                        url = director.photoUrl,
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
                        text = director.fio(),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(id = ru.pgk63.core_common.R.string.director),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.caption,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            if(director.cabinet != null) {
                Text(
                    text = stringResource(id = R.string.cabinet) +
                            " ${director.cabinet}",
                    color = PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )
            }

            if(director.information != null){
                Text(
                    text = director.information!!,
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