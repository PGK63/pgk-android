package ru.pgk63.core_common.api.techSupport.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.techSupport.TechSupportApi
import ru.pgk63.core_common.api.techSupport.model.ChatResponse
import ru.pgk63.core_common.api.techSupport.model.MessageContentType
import ru.pgk63.core_common.api.techSupport.model.SendMessageBody
import ru.pgk63.core_common.api.techSupport.model.UpdateMessageBody
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.extension.toByteArray
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class TechSupportRepository @Inject constructor(
    private val techSupportApi: TechSupportApi,
    @ApplicationContext private val context: Context
): ApiResponse() {

    suspend fun getChatAll(pageNumber: Int, pageSize: Int = PAGE_SIZE): ChatResponse {
        return techSupportApi.getChatAll(pageNumber, pageSize)
    }

    suspend fun sendMessageContent(
        messageId:Int,
        type: MessageContentType,
        file: Uri
    ): Result<Unit?> {

        val fileByteArray = file.toByteArray(context)

        val reqFile = fileByteArray.toRequestBody(
            "application/octet-stream".toMediaTypeOrNull(),
            0,
            fileByteArray.size
        )

        return safeApiCall {
            techSupportApi.sendMessageContent(
                id = messageId,
                type = type,
                file = MultipartBody.Part
                    .createFormData(
                        "file",
                        "file",
                        reqFile
                    )
            )
        }
    }

    suspend fun sendMessageContent(
        messageId:Int,
        type: MessageContentType,
        file: Bitmap
    ): Result<Unit?> {

        val outputStream= ByteArrayOutputStream()
        file.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val fileByteArray = outputStream.toByteArray()

        val reqFile = fileByteArray.toRequestBody(
            "application/octet-stream".toMediaTypeOrNull(),
            0,
            fileByteArray.size
        )

        return safeApiCall {
            techSupportApi.sendMessageContent(
                id = messageId,
                type = type,
                file = MultipartBody.Part
                    .createFormData(
                        "file",
                        "file",
                        reqFile
                    )
            )
        }
    }

    suspend fun sendMessage(body: SendMessageBody)  = safeApiCall { techSupportApi.sendMessage(body) }

    suspend fun pinMessage(messageId: Int) = techSupportApi.pinMessage(messageId)

    suspend fun deleteMessage(messageId: Int) = techSupportApi.deleteMessage(messageId)

    suspend fun updateMessage(messageId: Int, body: UpdateMessageBody) {
        techSupportApi.updateMessage(messageId, body)
    }

    suspend fun clearChat() = safeApiCall { techSupportApi.clearChat() }
}