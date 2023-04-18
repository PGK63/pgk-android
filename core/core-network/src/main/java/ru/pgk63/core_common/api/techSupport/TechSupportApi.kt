package ru.pgk63.core_common.api.techSupport

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.techSupport.model.*

interface TechSupportApi {

    @GET("/pgk63/api/TechnicalSupport/Chat")
    suspend fun getChatAll(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int = PAGE_SIZE
    ): ChatResponse

    @POST("/pgk63/api/TechnicalSupport/Chat/Message/{id}/Content")
    @Multipart
    suspend fun sendMessageContent(
        @Path("id") id:Int,
        @Query("type") type: MessageContentType,
        @Part file: MultipartBody.Part
    ): Response<Unit?>

    @POST("/pgk63/api/TechnicalSupport/Chat/Message")
    suspend fun sendMessage(@Body body: SendMessageBody): Response<Message>

    @PATCH("/pgk63/api/TechnicalSupport/Chat/Message/{id}/Pin")
    suspend fun pinMessage(@Path("id") messageId: Int)

    @DELETE("/pgk63/api/TechnicalSupport/Chat/Message/{id}")
    suspend fun deleteMessage(@Path("id") messageId: Int)

    @PUT("/pgk63/api/TechnicalSupport/Chat/Message/{id}")
    suspend fun updateMessage(@Path("id") messageId: Int, @Body body: UpdateMessageBody)

    @DELETE("/pgk63/api/TechnicalSupport/Chat")
    suspend fun clearChat(): Response<Unit>
}