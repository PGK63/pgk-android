package ru.pgk63.feature_tech_support.screen.chatScreen.viewModel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.techSupport.model.*
import ru.pgk63.core_common.api.techSupport.repository.TechSupportRepository
import ru.pgk63.core_common.api.techSupport.webSocket.MessagesWebSocket
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class ChatViewModel @Inject constructor(
    private val techSupportRepository: TechSupportRepository,
    userDataSource: UserDataSource
): ViewModel() {

    private val messagesWebSocket = MessagesWebSocket(userDataSource)

    val user = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    val responseMessages = messagesWebSocket.messageResponse.asStateFlow()

    private val _responseSendMessage = MutableStateFlow<Result<Message>?>(null)

    private val _responseSendMessageContent = MutableStateFlow<Result<Unit?>?>(null)
    val responseSendMessageContent = _responseSendMessageContent.asStateFlow()

    fun webSocketConnect() {
        messagesWebSocket.connect()
    }

    fun messagesParameters(parameters: MessageListParameters = MessageListParameters()) {
        messagesWebSocket.setMessagesParameters(parameters)
    }

    fun sendMessageContent(
        text: String,
        chatId: Int?,
        type: MessageContentType,
        file: Uri
    ){
        viewModelScope.launch {
            _responseSendMessageContent.value = Result.Loading()

            sendMessage(SendMessageBody(text = text, chatId = chatId))

            _responseSendMessage.first()?.data?.let { message ->
                _responseSendMessageContent.value = techSupportRepository.sendMessageContent(
                    messageId = message.id,
                    type = type,
                    file = file
                )
            }
        }
    }

    fun sendMessageContent(
        text: String,
        chatId: Int?,
        type: MessageContentType,
        file: Bitmap
    ){
        viewModelScope.launch {
            _responseSendMessageContent.value = Result.Loading()

            sendMessage(SendMessageBody(text = text, chatId = chatId))

            _responseSendMessage.first()?.data?.let { message ->
                _responseSendMessageContent.value = techSupportRepository.sendMessageContent(
                    messageId = message.id,
                    type = type,
                    file = file
                )
            }
        }
    }

    fun sendMessage(body: SendMessageBody) {
        viewModelScope.launch {
            _responseSendMessage.value = techSupportRepository.sendMessage(body)
        }
    }

    fun pinMessage(messageId: Int) {
        viewModelScope.launch {
            try {
                techSupportRepository.pinMessage(messageId)
            }catch (_:Exception){

            }
        }
    }

    fun deleteMessage(messageId: Int){
        viewModelScope.launch {
            try {
                techSupportRepository.deleteMessage(messageId)
            }catch (_:Exception){

            }
        }
    }

    fun updateMessage(messageId: Int, body: UpdateMessageBody){
        viewModelScope.launch {
            try {
                techSupportRepository.updateMessage(messageId, body)
            }catch (_:Exception){

            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            techSupportRepository.clearChat()
        }
    }

    override fun onCleared() {
        super.onCleared()
        messagesWebSocket.clear()
    }
}