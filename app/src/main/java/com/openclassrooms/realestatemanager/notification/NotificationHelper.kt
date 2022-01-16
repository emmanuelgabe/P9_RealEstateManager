package com.openclassrooms.realestatemanager.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.MainActivity
import com.openclassrooms.realestatemanager.utils.NOTIFICATION_REALESTATE_CHANNEL_ID


internal class NotificationHelper(
    private val context: Context,
    private val message: String
) {
    fun showRealEstateNotification() {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, pendingIntentFlags)
        val notificationBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_REALESTATE_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.real_estate_notification_title))
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification_house_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1, notificationBuilder.build())
        }
    }

}