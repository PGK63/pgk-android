package ru.pgk63.feature_settings.screens.settingsLanguageScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.language.model.Language
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.EmptyUi
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.TextFieldSearch
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.shimmer.VerticalListItemShimmer
import ru.pgk63.feature_settings.screens.settingsLanguageScreen.viewModel.SettingsLanguageViewModel
import java.util.*

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun SettingsLanguageScreenRoute(
    viewModel: SettingsLanguageViewModel = hiltViewModel(),
    onBackScreen: () -> Unit,
) {
    var searchText by remember { mutableStateOf("") }

    val languageList = viewModel.responseLanguageList.collectAsLazyPagingItems()
    var user by remember { mutableStateOf<UserLocalDatabase?>(null) }

    viewModel.user.onEach {
        user = it
    }.launchWhenStarted()

    LaunchedEffect(key1 = searchText, block = {
        viewModel.getLanguageList(search = searchText.ifEmpty { null })
    })

    SettingsLanguageScreen(
        user = user,
        languageList = languageList,
        searchText = searchText,
        onBackScreen = onBackScreen,
        onSearchTextChange = {
            searchText = it
        },
        updateLanguage = { languageId, languageCode ->
            viewModel.updateLanguage(languageId, languageCode)
        }
    )
}

@Composable
private fun SettingsLanguageScreen(
    user: UserLocalDatabase?,
    languageList: LazyPagingItems<Language>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onBackScreen: () -> Unit,
    updateLanguage:(languageId: Int, languageCode:String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = rememberToolbarScrollBehavior()

    val searchTextFieldFocusRequester = remember { FocusRequester() }
    var searchTextFieldVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.language),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
                actions = {
                    AnimatedVisibility(visible = !searchTextFieldVisible) {
                        IconButton(onClick = {
                            scope.launch {
                                searchTextFieldVisible = true
                                delay(100)
                                searchTextFieldFocusRequester.requestFocus()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = PgkTheme.colors.primaryText
                            )
                        }
                    }

                    AnimatedVisibility(visible = searchTextFieldVisible) {
                        TextFieldSearch(
                            text = searchText,
                            onTextChanged = onSearchTextChange,
                            modifier = Modifier
                                .focusRequester(searchTextFieldFocusRequester),
                            onClose = {
                                searchTextFieldVisible = false
                                onSearchTextChange("")
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (
            languageList.itemCount <= 0 && languageList.loadState.refresh !is LoadState.Loading
        ){
            EmptyUi()
        }else if(languageList.loadState.refresh is LoadState.Error) {
            ErrorUi()
        }else{
            LanguageListUi(
                user = user,
                languageList = languageList,
                contentPadding = paddingValues,
                updateLanguage = updateLanguage
            )
        }
    }
}

@Composable
private fun LanguageListUi(
    user: UserLocalDatabase?,
    languageList: LazyPagingItems<Language>,
    contentPadding: PaddingValues,
    updateLanguage:(languageId: Int, languageCode:String) -> Unit
) {
    val locale = remember { Locale.getDefault() }

    var currentLanguageCode by remember { mutableStateOf<String>(user?.languageCode ?: locale.language) }

    val isSelectedItem: (code: String) -> Boolean = { it == currentLanguageCode}

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {

        items(languageList) { language ->
            if(language != null){
                LanguageItemUi(
                    language = language,
                    isSelectedItem = isSelectedItem,
                    onClick = {
                        currentLanguageCode = language.code
                        updateLanguage(language.id, language.code)
                    }
                )
            }
        }

        if (languageList.loadState.append is LoadState.Loading){
            item {
                VerticalListItemShimmer()
            }
        }

        if (
            languageList.loadState.refresh is LoadState.Loading
        ){
            items(10) {
                VerticalListItemShimmer()
            }
        }
    }
}

@Composable
private fun LanguageItemUi(
    language: Language,
    isSelectedItem: (code: String) -> Boolean,
    onClick: () -> Unit
) {
    Row(
       modifier = Modifier
           .fillMaxWidth()
           .selectable(
               selected = isSelectedItem(language.code),
               onClick = onClick
           ),
       verticalAlignment = Alignment.CenterVertically
   ) {
        RadioButton(
            selected = isSelectedItem(language.code),
            onClick = null,
            modifier = Modifier.padding(15.dp),
            colors = RadioButtonDefaults.colors(
                unselectedColor = PgkTheme.colors.tintColor,
            )
        )

        Column {
            Text(
                text = language.name,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.body,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = language.nameEn,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.caption,
                fontFamily = PgkTheme.fontFamily.fontFamily,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
   }

    Divider(color = PgkTheme.colors.secondaryBackground)
}