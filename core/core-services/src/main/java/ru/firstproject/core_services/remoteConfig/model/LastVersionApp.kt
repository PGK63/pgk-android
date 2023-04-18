package ru.firstproject.core_services.remoteConfig.model

import java.util.*


data class LastVersionApp(
    val versionCode: Int,
    val versionName: String,
    val urlApk: String,
    val apkSizeMegabytes: Int,
    val date: Date,
    val title: String,
    val description: String,
    val isRequired: Boolean,
){
    fun isOldVersionApp(versionCode: Int): Boolean {
        return this.versionCode > versionCode
    }
}