package com.skillbox.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kirich1409.androidnotificationdsl.notification
import com.kirich1409.androidnotificationdsl.style.bigtext.bigTextStyle
import com.kirich1409.androidnotificationdsl.wearable.wearable

object NotificationChannels {

    fun isNotificationsEnabled(
        context: Context,
        notificationManager: NotificationManager
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(EVENT_CHANNEL_ID)
            if (channel == null) true
            else
                channel.importance == NotificationManager.IMPORTANCE_HIGH && NotificationManagerCompat.from(
                    context
                ).areNotificationsEnabled()
        } else
            NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    private const val EVENT_CHANNEL_ID = "notifier_background"
    const val EVENT_ID = 245425

    @RequiresApi(Build.VERSION_CODES.N)
    fun buildNotificationEvent(
        context: Context,
        title: String,
        message: String,
        @DrawableRes iconRes: Int
    ) =
        notification(context, EVENT_CHANNEL_ID, iconRes) {
            contentTitle(title)
            ongoing(false)
            onlyAlertOnce(true)
            vibrate(longArrayOf(100, 200, 500, 500))
            priority(NotificationManager.IMPORTANCE_HIGH)
            category(NotificationCompat.CATEGORY_EVENT)
            wearable {
                contentIntentAvailableOffline = false
            }
            bigTextStyle { message }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context) {
        val name = "Notification"
        val channelDescription = "Strava runner notification"
        val priority = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(EVENT_CHANNEL_ID, name, priority).apply {
            description = channelDescription
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 500, 500)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}