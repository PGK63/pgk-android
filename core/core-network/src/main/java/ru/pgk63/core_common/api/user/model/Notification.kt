package ru.pgk63.core_common.api.user.model

import java.util.Date

data class NotificationResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results: List<Notification>
)

data class Notification(
    val id:Int,
    val title:String,
    val message:String,
    val date: Date
)