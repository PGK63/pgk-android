package ru.pgk63.pgk

import android.app.Application
import com.google.firebase.FirebaseOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.user.repository.UserRepository
import ru.pgk63.pgk.services.fcm.FCM
import ru.pgk63.pgk.utils.FirebaseConstants
import javax.inject.Inject

@HiltAndroidApp
class PgkApplication: Application() {

    @Inject lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()

        firebaseInitialize()

        fcmSaveToken()

        updateUserSettings()
    }

    private fun fcmSaveToken() {
        FCM.saveToken(this)
    }

    private fun updateUserSettings() {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.getSettings()
        }
    }

    private fun firebaseInitialize() {
        val options = FirebaseOptions.Builder()
            .setProjectId(FirebaseConstants.PROJECT_ID)
            .setApplicationId(FirebaseConstants.APPLICATION_ID)
            .build()

        Firebase.initialize(this, options, FirebaseConstants.PROJECT_ID)
    }
}