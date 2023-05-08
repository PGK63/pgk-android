package ru.pgk63.feature_group.screens.groupDetailsScreen.model

internal sealed interface GroupDetailsBottomDrawerContentState {
    object Empty: GroupDetailsBottomDrawerContentState
    object UpdateCourse: GroupDetailsBottomDrawerContentState
    object DownloadVedomost: GroupDetailsBottomDrawerContentState
}