package ru.firstproject.core_services.remoteConfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.GsonBuilder
import ru.firstproject.core_services.remoteConfig.model.LastVersionApp
import ru.pgk63.core_common.extension.fromJson
import javax.inject.Inject

class RemoteConfigService @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .create()

    fun getLastVersionApp(): LastVersionApp? {
        return try {
            remoteConfig.fetchAndActivate()

            val appVersionJson = remoteConfig.getValue(RemoteConfigUtils.APP_VERSION).asString()

            if(appVersionJson.isNotEmpty())
                gson.fromJson(appVersionJson)
            else
                null
        }catch (e:Exception){
            null
        }
    }
}