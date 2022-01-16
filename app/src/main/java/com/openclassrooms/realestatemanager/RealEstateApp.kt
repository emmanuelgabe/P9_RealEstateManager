package com.openclassrooms.realestatemanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.openclassrooms.realestatemanager.utils.NOTIFICATION_REALESTATE_CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RealEstateApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val realEstateChannel =
                NotificationChannel(
                    NOTIFICATION_REALESTATE_CHANNEL_ID,
                    getString(R.string.real_estate_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = getString(R.string.real_estate_notification_channel_description)
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(realEstateChannel)
        }
    }
}