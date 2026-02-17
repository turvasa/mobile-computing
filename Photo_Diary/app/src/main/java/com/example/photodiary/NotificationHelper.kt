package com.example.photodiary

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri


/**
 * Helper class for managing the app notifications.
 * Provides functions for creating notification channels and displaying notifications.
 */
class NotificationHelper {

    private val channelId = "photo_diary"


    /**
     * Displays a notification with the given title and message
     *
     * @param context Context used to create and send the notification.
     * @param title Notification title.
     * @param message Notification body text.
     */
    fun showNotification(context: Context, title: String, message: String) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = "photodiary://add".toUri()
        }
        val pendingIntent = android.app.PendingIntent.getActivity(
            context, 0, intent,
            android.app.PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Check permissions
        if (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        // Send the notification
        NotificationManagerCompat.from(context).notify(1, notification.build())
    }


    /**
     * Creates a notification channel for API level >= 26.
     *
     * @param context Context used to create the notification channel.
     */
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Diary alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Diary daily reminder"
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}
