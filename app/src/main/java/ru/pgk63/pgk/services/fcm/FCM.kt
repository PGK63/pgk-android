package ru.pgk63.pgk.services.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_ui.R
import ru.pgk63.pgk.activity.MainActivity
import kotlin.random.Random

class FCM: FirebaseMessagingService() {

    companion object {

        var sharedPref: SharedPreferences? = null

        private const val SHARED_FCM_TOKEN_KEY = "fcm_token"
        private const val CHANNEL_ID = "0"

        private var token:String?
            get(){
                return sharedPref?.getString(SHARED_FCM_TOKEN_KEY,"")
            }
            set(value) {
                sharedPref?.edit()
                    ?.putString(SHARED_FCM_TOKEN_KEY, value)
                    ?.apply()
            }

        fun saveToken(context: Context) {
            sharedPref = context.getSharedPreferences(SHARED_FCM_TOKEN_KEY,Context.MODE_PRIVATE)
            FirebaseInstallations.getInstance().getToken(true).addOnSuccessListener  {
                token = it.token
            }
        }

        fun subscribeToTopics(firebaseMessaging: FirebaseMessaging, user: UserLocalDatabase) {
            if(user.statusRegistration && user.userId != 0){
                firebaseMessaging.subscribeToTopic("topics/${user.userId}")
                firebaseMessaging.subscribeToTopic("topics/user_${user.userId}")

                if(user.userRole == UserRole.ADMIN){
                    firebaseMessaging.subscribeToTopic("topics/admin")
                }
            }else {
                unsubscribeToTopics(
                    firebaseMessaging = firebaseMessaging,
                    user = user
                )
            }
        }

        private fun unsubscribeToTopics(firebaseMessaging: FirebaseMessaging, user: UserLocalDatabase) {

            firebaseMessaging.unsubscribeFromTopic("topics/${user.userId}")
            firebaseMessaging.unsubscribeFromTopic("topics/user_${user.userId}")

            if(user.userRole == UserRole.ADMIN){
                firebaseMessaging.unsubscribeFromTopic("topics/admin")
            }
        }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
    }

    override fun onMessageReceived(message: RemoteMessage) {

        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.pgk_icon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "Notifications"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "Default notifications"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}