package ru.pgk63.core_common.api.techSupport.webSocket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.*
import ru.pgk63.core_common.api.techSupport.model.MessageListParameters
import ru.pgk63.core_common.api.techSupport.model.MessageResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.decodeFromString
import ru.pgk63.core_common.extension.encodeToString
import ru.pgk63.core_database.user.UserDataSource

class MessagesWebSocket (
    private val userDataSource: UserDataSource
) : WebSocketListener() {

    private companion object {
        const val DELAY_WEB_SOCKET_CONNECT = 100L
    }

    private var webSocket: WebSocket? = null

    private var messagesParameters = MessageListParameters()
    var messageResponse = MutableStateFlow<Result<MessageResponse>>(Result.Loading())

    fun connect() {
        val request = Request.Builder()
            .url("wss://api.cfif31.ru/pgk63/ws/Chat/Message")
            .addHeader("Authorization","Bearer ${userDataSource.getAccessToken()}")
            .build()

        val client = OkHttpClient()

        client.newWebSocket(request, this)
        client.dispatcher.executorService.shutdown()
    }


    private fun sendMessageDetail(postPayload: String) {
        if (webSocket != null) {
            webSocket?.send(postPayload)
        }
    }

    fun setMessagesParameters(messagesParameters: MessageListParameters){
        this.messagesParameters = messagesParameters
    }

    fun clear() {
        messageResponse.value = Result.Loading()

        if (webSocket != null) {
            webSocket?.cancel()
            webSocket = null
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocket.send(messagesParameters.encodeToString())
        this.webSocket = webSocket
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) = Unit

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) = Unit

    override fun onMessage(webSocket: WebSocket, text: String) {

        messageResponse.value = Result.Success(text.decodeFromString())

        CoroutineScope(Dispatchers.Main).launch {
            delay(DELAY_WEB_SOCKET_CONNECT)
            sendMessageDetail(messagesParameters.encodeToString())
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        messageResponse.value = Result.Error(response?.message ?: t.message ?: "")
    }
}