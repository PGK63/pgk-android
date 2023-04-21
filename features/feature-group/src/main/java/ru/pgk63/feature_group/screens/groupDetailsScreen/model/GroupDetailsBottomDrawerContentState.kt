package ru.pgk63.feature_group.screens.groupDetailsScreen.model

internal sealed interface GroupDetailsBottomDrawerContentState {
    object Empty: GroupDetailsBottomDrawerContentState
    data class UpdateCourse(val groupId: Int): GroupDetailsBottomDrawerContentState
}