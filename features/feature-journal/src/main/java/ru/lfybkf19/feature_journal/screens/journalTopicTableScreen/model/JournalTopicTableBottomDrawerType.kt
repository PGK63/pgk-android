package ru.lfybkf19.feature_journal.screens.journalTopicTableScreen.model

sealed class JournalTopicTableBottomDrawerType {
    object CreateTopic: JournalTopicTableBottomDrawerType()
    data class TopicRowMenu(val topic: ru.pgk63.core_model.journal.JournalTopic): JournalTopicTableBottomDrawerType()
}
