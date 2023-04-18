package ru.pgk63.core_common.api.user.model

data class NotificationSetting(
    val includedNotifications: Boolean,
    val soundNotifications: Boolean,
    val vibrationNotifications: Boolean,
    val includedSchedulesNotifications: Boolean,
    val includedJournalNotifications: Boolean,
    val includedRaportichkaNotifications: Boolean,
    val includedTechnicalSupportNotifications: Boolean
)