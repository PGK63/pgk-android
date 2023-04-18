package ru.pgk63.feature_tech_support.screen.chatListScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ru.pgk63.core_common.api.techSupport.model.Chat
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.TopBarBack
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.view.EmptyUi
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.core_ui.view.ImageCoil
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.shimmer.VerticalListItemShimmer
import ru.pgk63.feature_tech_support.screen.chatListScreen.viewModel.ChatListViewModel

@Composable
internal fun ChatListRoute(
    viewModel: ChatListViewModel = hiltViewModel(),
    onBackScreen: () -> Unit = {},
    onChatScreen: (chatId: Int) -> Unit = {},
) {
    val chats = viewModel.chats.collectAsLazyPagingItems()

    ChatListScreen(
        chats = chats,
        onBackScreen = onBackScreen,
        onChatScreen = onChatScreen
    )
}

@Composable
private fun ChatListScreen(
    chats: LazyPagingItems<Chat>,
    onBackScreen: () -> Unit = {},
    onChatScreen: (chatId: Int) -> Unit = {}
) {
    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = stringResource(id = R.string.help),
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen
            )
        },
        content = { paddingValues ->

            if (
                chats.itemCount <= 0 && chats.loadState.refresh !is LoadState.Loading
            ){
                EmptyUi()
            }else if(chats.loadState.refresh is LoadState.Error) {
                ErrorUi()
            }else{
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(chats){ chat ->
                        chat?.let {
                            ChatItemUi(chat = chat, onClick = { onChatScreen(chat.id) })
                        }
                    }

                    if (chats.loadState.append is LoadState.Loading){
                        item {
                            VerticalListItemShimmer()
                        }
                    }

                    if (
                        chats.loadState.refresh is LoadState.Loading
                    ){
                        items(20) {
                            VerticalListItemShimmer()
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ChatItemUi(
    chat: Chat,
    onClick: () -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Card(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        shape = PgkTheme.shapes.cornersStyle,
        modifier = Modifier.padding(5.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                if(chat.lastMessage.user.photoUrl != null) {
                    ImageCoil(
                        url = chat.lastMessage.user.photoUrl,
                        modifier = Modifier
                            .width((screenWidthDp / 5).dp)
                            .height((screenHeightDp / 9.6).dp)
                            .clip(AbsoluteRoundedCornerShape(90.dp))
                            .padding(5.dp)
                    )
                }else {
                    Image(
                        painter = painterResource(id = R.drawable.profile_photo),
                        contentDescription = null,
                        modifier = Modifier
                            .width((screenWidthDp / 5).dp)
                            .height((screenHeightDp / 9.6).dp)
                            .padding(5.dp)
                    )
                }

                Column {
                    Text(
                        text = "${chat.lastMessage.user.lastName} ${chat.lastMessage.user.firstName} " +
                                (chat.lastMessage.user.middleName ?: ""),
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.caption,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        modifier = Modifier.padding(5.dp),
                        fontWeight = FontWeight.W700
                    )

                    chat.lastMessage.text?.let { messageText ->
                        Text(
                            text = messageText,
                            color = PgkTheme.colors.primaryText,
                            style = PgkTheme.typography.caption,
                            fontFamily = PgkTheme.fontFamily.fontFamily,
                            modifier = Modifier.padding(5.dp),
                            fontWeight = FontWeight.W100
                        )
                    }
                }
            }
        }
    }
}