package ru.pgk63.feature_main.screens.notificationListScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ru.pgk63.core_common.api.user.model.Notification
import ru.pgk63.core_common.extension.parseToBaseDateFormat
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.EmptyUi
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.feature_main.screens.notificationListScreen.viewModel.NotificationListViewModel

@Composable
internal fun NotificationListRoute(
    viewModel: NotificationListViewModel = hiltViewModel(),
    onBackScreen: () -> Unit
) {
    val notifications = viewModel.responseNotificationList.collectAsLazyPagingItems()

    val notificationsSearchText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = notificationsSearchText, block = {
        viewModel.getNotifications(search = notificationsSearchText.ifEmpty { null })
    })

    NotificationListScreen(
        notifications = notifications,
        onBackScreen = onBackScreen
    )
}

@Composable
private fun  NotificationListScreen(
    notifications: LazyPagingItems<Notification>,
    onBackScreen: () -> Unit
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            Column {
                TopBarBack(
                    title = stringResource(id = R.string.notifications),
                    scrollBehavior = scrollBehavior,
                    onBackClick = onBackScreen
                )
            }
        }
    ) { paddingValues ->
        if (
            notifications.itemCount <= 0 && notifications.loadState.refresh !is LoadState.Loading
        ){
            EmptyUi()
        }else if(notifications.loadState.refresh is LoadState.Error) {
            ErrorUi()
        }else{
            NotificationsList(
                notifications = notifications,
                paddingValues = paddingValues
            )
        }
    }
}

@Composable
private fun NotificationsList(
    notifications: LazyPagingItems<Notification>,
    paddingValues: PaddingValues
) {
    LazyColumn(
        contentPadding = paddingValues
    ) {
        items(notifications) { notification ->
            if(notification != null){
                NotificationItem(notification = notification)
            }
        }
    }
}

@Composable
private fun NotificationItem(notification: Notification) {
    Column {
        Divider(color = PgkTheme.colors.secondaryBackground)

        Text(
            text = notification.title,
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(5.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = notification.message,
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.body,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(15.dp)
        )

        Text(
            text = notification.date.parseToBaseDateFormat(),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.caption,
            fontFamily = PgkTheme.fontFamily.fontFamily,
            modifier = Modifier.padding(5.dp).fillMaxWidth(),
            textAlign = TextAlign.End
        )

        Divider(color = PgkTheme.colors.secondaryBackground)
    }
}