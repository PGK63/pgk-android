package ru.pgk63.feature_tech_support.screen.chatScreen

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.onEach
import ru.pgk63.core_common.api.techSupport.model.*
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.compose.annotatedLinkString
import ru.pgk63.core_common.extension.copyToClipboard
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.extension.openBrowser
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.icon.ResIcons
import ru.pgk63.core_ui.view.*
import ru.pgk63.core_ui.view.collapsingToolbar.rememberToolbarScrollBehavior
import ru.pgk63.core_ui.view.metaBalls.comonents.LoadingUi
import ru.pgk63.feature_tech_support.screen.chatScreen.enums.AttachMenu
import ru.pgk63.feature_tech_support.screen.chatScreen.enums.ChatMenu
import ru.pgk63.feature_tech_support.screen.chatScreen.enums.MessageMenu
import ru.pgk63.feature_tech_support.screen.chatScreen.viewModel.ChatViewModel

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun ChatRoute(
    viewModel: ChatViewModel = hiltViewModel(),
    chatId: Int? = null,
    onBackScreen: () -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    var user by remember { mutableStateOf(UserLocalDatabase()) }
    var sendMessageContentResult by remember { mutableStateOf<Result<Unit?>?>(null) }

    var messageText by remember { mutableStateOf("") }
    var messagesResult by remember { mutableStateOf<Result<MessageResponse>>(Result.Loading()) }
    val messagesParameter by remember { mutableStateOf(MessageListParameters(chatId = chatId)) }

    var searchMode by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    var pinMode by remember { mutableStateOf(false) }

    val singleGalleryImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.sendMessageContent(
                    text = messageText,
                    type = MessageContentType.IMAGE,
                    chatId = chatId,
                    file = uri
                )
            }
        }
    )

    val singleGalleryVideoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.sendMessageContent(
                    text = messageText,
                    type = MessageContentType.VIDEO,
                    chatId = chatId,
                    file = uri
                )
            }
        }
    )

    val singleCameraImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                viewModel.sendMessageContent(
                    text = messageText,
                    type = MessageContentType.IMAGE,
                    chatId = chatId,
                    file = bitmap
                )
            }
        }
    )

    val singleFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                viewModel.sendMessageContent(
                    text = messageText,
                    type = MessageContentType.FILE,
                    chatId = chatId,
                    file = uri
                )
            }
        }
    )

    viewModel.user.onEach {
        user = it
    }.launchWhenStarted()

    viewModel.responseMessages.onEach {
        messagesResult = it
    }.launchWhenStarted()

    viewModel.responseSendMessageContent.onEach {  result ->
        sendMessageContentResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.messagesParameters(messagesParameter)
        viewModel.webSocketConnect()
    })

    LaunchedEffect(key1 = sendMessageContentResult, block = {
        when(sendMessageContentResult) {
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.error)
                )
            }
            is Result.Loading -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.loading)
                )
            }
            is Result.Success -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.success)
                )
            }
            null -> Unit
        }
    })

    LaunchedEffect(key1 = Unit, block = {
        messagesParameter.userId = if (chatId == null) user.userId else null

        viewModel.messagesParameters(messagesParameter)
    })

    LaunchedEffect(searchText, searchMode, block = {
        messagesParameter.search = if(searchMode && searchText.isNotEmpty()) searchText else null

        viewModel.messagesParameters(messagesParameter)
    })

    LaunchedEffect(key1 = pinMode, block = {
        messagesParameter.pin = if(!pinMode) null else true
        viewModel.messagesParameters(messagesParameter)
    })

    ChatScreen(
        user = user,
        messagesResult = messagesResult,
        pinMode = pinMode,
        searchMode = searchMode,
        searchText = searchText,
        messageText = messageText,
        onMessageChange = { messageText = it },
        onSearchModeChange = { searchMode = it },
        onSearchTextChange = { searchText = it },
        onPunModeChange = { pinMode = it },
        onBackScreen = onBackScreen,
        sendMessage = {
            viewModel.sendMessage(SendMessageBody(text = messageText, chatId = chatId))

            messageText = ""
        },
        pinMessage = { messageId ->
            viewModel.pinMessage(messageId)
        },
        deleteMessage = { messageId ->
            viewModel.deleteMessage(messageId)
        },
        editMessage = { message ->
            viewModel.updateMessage(
                messageId = message.id,
                body = UpdateMessageBody(
                    text = messageText
                )
            )

            messageText = ""
        },
        onClickAttachCamera = {
            singleCameraImagePickerLauncher.launch()
        },
        onClickAttachVideo = {
            singleGalleryVideoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
            )
        },
        onClickAttachGallery = {
            singleGalleryImagePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onClickAttachFile = {
            singleFilePickerLauncher.launch(arrayOf(
                "application/pdf",
                "application/msword",
                "application/ms-doc",
                "application/doc",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "text/plain"
            ))
        },
        clearChat = {
            viewModel.clearChat()
        }
    )
}

@Composable
private fun ChatScreen(
    user: UserLocalDatabase,
    messagesResult: Result<MessageResponse>,
    searchMode: Boolean = false,
    pinMode: Boolean = false,
    searchText: String = "",
    messageText:String = "",
    onMessageChange: (String) -> Unit = {},
    onSearchModeChange: (Boolean) -> Unit = {},
    onPunModeChange: (Boolean) -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onBackScreen: () -> Unit = {},
    sendMessage: () -> Unit = {},
    pinMessage: (messageId: Int) -> Unit = {},
    deleteMessage: (messageId: Int) -> Unit = {},
    editMessage: (message: Message) -> Unit = {},
    clearChat: () -> Unit,
    onClickAttachCamera: () -> Unit,
    onClickAttachGallery: () -> Unit,
    onClickAttachFile: () -> Unit,
    onClickAttachVideo: () -> Unit,
) {
    var chatMenu by remember { mutableStateOf(false) }
    var editMessageAlertDialog by remember { mutableStateOf(false) }
    var clickMessageEdit by remember { mutableStateOf<Message?>(null) }

    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopBarBack(
                title = if(!searchMode) stringResource(id = R.string.help) else "",
                scrollBehavior = scrollBehavior,
                onBackClick = onBackScreen,
                actions = {
                    Column {
                        AnimatedVisibility(visible = searchMode) {
                            TextFieldSearch(
                                text = searchText,
                                onTextChanged = onSearchTextChange,
                                onClose = {
                                    onSearchTextChange("")
                                    onSearchModeChange(false)
                                }
                            )
                        }

                        AnimatedVisibility(visible = !searchMode) {
                            IconButton(onClick = { chatMenu = !chatMenu }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null,
                                    tint = PgkTheme.colors.primaryText
                                )
                            }

                            ChatMenuUi(
                                visible = chatMenu,
                                pinMode = pinMode,
                                onDismissRequest = { chatMenu = false },
                                onClick = { chatMenu ->
                                    when(chatMenu){
                                        ChatMenu.SEARCH_MESSAGES -> onSearchModeChange(true)
                                        ChatMenu.PIN_MESSAGES -> onPunModeChange(!pinMode)
                                        ChatMenu.CLEAR_CHAT -> clearChat()
                                    }
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomBarUi(
                sendMessage = sendMessage,
                messageText = messageText,
                onMessageChange = onMessageChange,
                onClickAttachCamera = onClickAttachCamera,
                onClickAttachGallery = onClickAttachGallery,
                onClickAttachFile = onClickAttachFile,
                onClickAttachVideo = onClickAttachVideo
            )
        },
        content = { paddingValues ->
            when(messagesResult){
                is Result.Error -> ErrorUi(messagesResult.message)
                is Result.Loading -> LoadingUi()
                is Result.Success -> {
                    if(editMessageAlertDialog){
                        EditMessageAlertDialog(
                            messageText = messageText,
                            onMessageChange = onMessageChange,
                            editMessage = {
                                clickMessageEdit?.let { editMessage(it) }
                                editMessageAlertDialog = false
                            },
                            onDismissRequest = {
                                editMessageAlertDialog = false
                            }
                        )
                    }

                    Messages(
                        messages = messagesResult.data!!.messages,
                        user = user,
                        bottomBarPadding = paddingValues.calculateBottomPadding(),
                        pinMessage = pinMessage,
                        deleteMessage = deleteMessage,
                        editMessage = { message ->
                            message.text?.let { onMessageChange(it) }
                            editMessageAlertDialog = true
                            clickMessageEdit = message
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun ChatMenuUi(
    visible: Boolean,
    pinMode: Boolean = false,
    onDismissRequest: () -> Unit,
    onClick: (ChatMenu) -> Unit
) {
    DropdownMenu(
        expanded = visible,
        modifier = Modifier.background(PgkTheme.colors.secondaryBackground),
        onDismissRequest = onDismissRequest
    ) {
        ChatMenu.values().forEach { chatMenu ->
            DropdownMenuItem(
                onClick = {
                    onClick(chatMenu)
                    onDismissRequest()
                }
            ) {
                Icon(
                    imageVector = chatMenu.icon,
                    contentDescription = null,
                    tint = if(chatMenu == ChatMenu.CLEAR_CHAT)
                        PgkTheme.colors.errorColor
                    else if(pinMode && chatMenu == ChatMenu.PIN_MESSAGES)
                        PgkTheme.colors.tintColor
                    else
                        PgkTheme.colors.primaryText
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = stringResource(id = chatMenu.nameId),
                    color = if(chatMenu == ChatMenu.CLEAR_CHAT)
                        PgkTheme.colors.errorColor
                    else if(pinMode && chatMenu == ChatMenu.PIN_MESSAGES)
                        PgkTheme.colors.tintColor
                    else
                        PgkTheme.colors.primaryText,
                    style = PgkTheme.typography.caption,
                    fontFamily = PgkTheme.fontFamily.fontFamily
                )
            }
        }
    }
}

@Composable
private fun BottomBarUi(
    messageText:String,
    onMessageChange: (String) -> Unit = {},
    sendMessage: () -> Unit = {},
    onClickAttachCamera: () -> Unit,
    onClickAttachGallery: () -> Unit,
    onClickAttachFile: () -> Unit,
    onClickAttachVideo: () -> Unit,
) {
    var attachMenuUi by remember { mutableStateOf(false) }

    AnimatedVisibility(visible = !attachMenuUi) {
        SentMessageTextField(
            messageText = messageText,
            onMessageChange = onMessageChange,
            sendMessage = sendMessage,
            openAttachMenu = {
                attachMenuUi = true
            }
        )
    }

    AnimatedVisibility(visible = attachMenuUi) {
        AttachMenuUi(
            onClick = { attachMenu ->
                when(attachMenu){
                    AttachMenu.BACK -> attachMenuUi = false
                    AttachMenu.CAMERA -> onClickAttachCamera()
                    AttachMenu.GALLERY -> onClickAttachGallery()
                    AttachMenu.FILE -> onClickAttachFile()
                    AttachMenu.VIDEO -> onClickAttachVideo()
                }
            }
        )
    }
}

@Composable
private fun SentMessageTextField(
    messageText:String,
    onMessageChange: (String) -> Unit = {},
    sendMessage: () -> Unit = {},
    openAttachMenu: (() -> Unit)? = null
) {
    TextField(
        value = messageText,
        onValueChange = onMessageChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(id = R.string.message),
                color = PgkTheme.colors.primaryText
            )
        },
        colors = rememberTextFieldColors(
            focusedIndicatorColor = PgkTheme.colors.primaryBackground
        ),
        leadingIcon = {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.EmojiEmotions,
                    contentDescription = null,
                    tint = PgkTheme.colors.primaryText
                )
            }
        },
        trailingIcon = {
            Row {
                AnimatedVisibility(
                    visible = messageText.isNotEmpty()
                ) {
                    IconButton(onClick = sendMessage) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.padding(5.dp),
                            tint = PgkTheme.colors.primaryText
                        )
                    }
                }

                if(openAttachMenu != null){
                    IconButton(onClick = openAttachMenu) {
                        Icon(
                            painterResource(id = ResIcons.attachFile),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(30.dp),
                            tint = PgkTheme.colors.primaryText
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AttachMenuUi(
    onClick:(AttachMenu) -> Unit
) {
    LazyRow(verticalAlignment = Alignment.CenterVertically) {
        item {
            AttachMenu.values().forEach { attachMenu ->
                Card(
                    backgroundColor = PgkTheme.colors.primaryBackground,
                    onClick = { onClick(attachMenu) }
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            shape = CircleShape,
                            backgroundColor = PgkTheme.colors.tintColor,
                            modifier = Modifier.padding(5.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = attachMenu.iconId),
                                contentDescription = null,
                                tint = PgkTheme.colors.primaryText,
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(5.dp)
                            )
                        }

                        Text(
                            text = stringResource(id = attachMenu.nameId),
                            color = PgkTheme.colors.primaryText,
                            modifier = Modifier.padding(5.dp),
                            style = PgkTheme.typography.caption,
                            fontFamily = PgkTheme.fontFamily.fontFamily
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Messages(
    messages: List<Message>,
    user: UserLocalDatabase,
    bottomBarPadding: Dp,
    pinMessage: (messageId: Int) -> Unit,
    deleteMessage: (messageId: Int) -> Unit,
    editMessage: (message: Message) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        reverseLayout = true,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(bottomBarPadding))
        }

        items(messages){ message ->

            var messageMenuVisible by remember { mutableStateOf(false) }

            MessageUi(
                message = message,
                user = user,
                onClick = { messageMenuVisible = true }
            )

            MessageMenuUi(
                visible = messageMenuVisible,
                onVisibleChange = { messageMenuVisible = false },
                editOrDeleteMessageVisibly = user.userId == message.user.id,
                onClick = { messageMenu ->
                    when(messageMenu){
                        MessageMenu.COPY -> message.text?.let { context.copyToClipboard(it) }
                        MessageMenu.PIN -> pinMessage(message.id)
                        MessageMenu.EDIT -> editMessage(message)
                        MessageMenu.DELETE -> deleteMessage(message.id)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MessageUi(
    message: Message,
    user: UserLocalDatabase,
    screenWidthDp: Int = LocalConfiguration.current.screenWidthDp,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(user.userId == message.user.id)
            Arrangement.End
        else
            Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = (screenWidthDp / 1.5).dp)
                .padding(5.dp),
            backgroundColor = if(user.userId == message.user.id)
                PgkTheme.colors.tintColor
            else
                PgkTheme.colors.secondaryBackground,
            shape = PgkTheme.shapes.cornersStyle,
            onClick = onClick
        ) {
            Column {

                if(message.text != null && message.text!!.isNotEmpty()){
                    Text(
                        text = message.text!!,
                        color = PgkTheme.colors.primaryText,
                        modifier = Modifier.padding(
                            start = 15.dp,
                            top = 15.dp,
                            end = 15.dp
                        ),
                        style = PgkTheme.typography.body,
                        fontFamily = PgkTheme.fontFamily.fontFamily
                    )
                }

                message.contents.forEach { content ->
                    if(content.type == MessageContentType.IMAGE) {
                        ImageCoil(
                            url = content.url,
                            modifier = Modifier
                                .padding(5.dp)
                                .width((screenWidthDp / 1.5).dp)
                                .fillMaxHeight()
                        )
                    }else {
                        ClickableText(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            text = annotatedLinkString(
                                text = stringResource(id = content.type.nameId),
                                url = content.url,
                                fontSize = PgkTheme.typography.body.fontSize,
                                fontFamily = PgkTheme.fontFamily.fontFamily
                            ),
                            style = PgkTheme.typography.body,
                            onClick = {
                                context.openBrowser(content.url)
                            }
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(15.dp)
                ) {

                    AnimatedVisibility(visible = message.pin){
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = null,
                            tint = PgkTheme.colors.primaryText,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))
                    }

                    AnimatedVisibility(visible = message.edited){
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = PgkTheme.colors.primaryText,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))
                    }

                    Text(
                        text = message.date,
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.caption,
                        fontFamily = PgkTheme.fontFamily.fontFamily,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageMenuUi(
    visible: Boolean,
    editOrDeleteMessageVisibly: Boolean = false,
    onVisibleChange: () -> Unit,
    onClick: (MessageMenu) -> Unit
) {
    if(visible){
        AlertDialog(
            backgroundColor = Color.Transparent,
            onDismissRequest = onVisibleChange,
            buttons = {
                MessageMenu.values().forEach { messageMenu ->

                    if(messageMenu == MessageMenu.DELETE || messageMenu == MessageMenu.EDIT) {
                        if(editOrDeleteMessageVisibly) {
                            MessageMenuItemUi(
                                messageMenu = messageMenu,
                                onVisibleChange = onVisibleChange,
                                onClick = { onClick(messageMenu) }
                            )
                        }
                    }else {
                        MessageMenuItemUi(
                            messageMenu = messageMenu,
                            onVisibleChange = onVisibleChange,
                            onClick = { onClick(messageMenu) }
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun MessageMenuItemUi(
    messageMenu: MessageMenu,
    onVisibleChange: () -> Unit,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        modifier = Modifier.background(PgkTheme.colors.secondaryBackground),
        onClick = {
            onClick()
            onVisibleChange()
        }
    ) {
        Icon(
            imageVector = messageMenu.icon,
            contentDescription = null,
            tint = if(messageMenu == MessageMenu.DELETE)
                PgkTheme.colors.errorColor
            else
                PgkTheme.colors.primaryText,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = stringResource(id = messageMenu.nameId),
            color = if(messageMenu == MessageMenu.DELETE)
                PgkTheme.colors.errorColor
            else
                PgkTheme.colors.primaryText,
            style = PgkTheme.typography.caption,
            fontFamily = PgkTheme.fontFamily.fontFamily
        )
    }
}

@Composable
private fun EditMessageAlertDialog(
    messageText:String,
    onMessageChange: (String) -> Unit = {},
    editMessage: () -> Unit = {},
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        backgroundColor = PgkTheme.colors.secondaryBackground,
        shape = PgkTheme.shapes.cornersStyle,
        onDismissRequest = onDismissRequest,
        buttons = {
            SentMessageTextField(
                messageText = messageText,
                onMessageChange = onMessageChange,
                sendMessage = editMessage
            )
        }
    )
}