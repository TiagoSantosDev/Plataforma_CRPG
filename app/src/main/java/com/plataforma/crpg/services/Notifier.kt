package com.plataforma.crpg.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.plataforma.crpg.R
import com.plataforma.crpg.ui.MainActivity

class Notifier(val context: Context) {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName = context.getString(R.string.action_cancel)
            val channelDescription = context.getString(R.string.action_settings)
            val channelId = channelName
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    fun sendNotification(title: String, content: String){

        val notification = transportsNotification(title, content)

        with(NotificationManagerCompat.from(context)){
            val notificationId = title.hashCode() //pretty much unique
            notify(notificationId, notification)
        }

    }


    private fun transportsNotification(title: String, content: String) : Notification {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        intent.putExtra("title",title)

        val requestCode = title.hashCode()
        val pendingIntent = PendingIntent.getActivity(context,requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, "TransportRem")
        builder.setSmallIcon(R.drawable.ic_notification_bus)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setContentIntent(pendingIntent)
        return builder.build()
    }




}