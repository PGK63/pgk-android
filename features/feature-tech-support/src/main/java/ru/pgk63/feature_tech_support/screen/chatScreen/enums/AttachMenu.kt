package ru.pgk63.feature_tech_support.screen.chatScreen.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.icon.ResIcons

internal enum class AttachMenu(@StringRes val nameId: Int,@DrawableRes val iconId: Int) {
    BACK(R.string.back, ResIcons.back),
    CAMERA(R.string.camera, ResIcons.camera),
    GALLERY(R.string.gallery, ResIcons.gallery),
    FILE(R.string.file, ResIcons.file),
    VIDEO(R.string.video, ResIcons.videoCamera)
}