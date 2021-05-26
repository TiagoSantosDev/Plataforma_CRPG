package com.plataforma.crpg.services

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.plataforma.crpg.R
import com.plataforma.crpg.ui.MainActivity

class NotificationsHandler: IntentService("NotificationsHandler") {

    val ACTION1 = "reminder"
    val ACTION2 = ""

    override fun onHandleIntent(p0: Intent?) {
        val action = p0?.action;
        println("Action onHandleIntent" + action)
        if (ACTION1 == action) {
            println("Entrou aqui")
            val intent = Intent(this, MainActivity::class.java).apply {
                println("Entrou no intent")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                /*
                val fragment: Fragment = TransportsSelectionFragment()
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()*/
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = NotificationChannel("YOUR_CHANNEL_ID",
                        "YOUR_CHANNEL_NAME",
                        NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
                mNotificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
                    .setSmallIcon(R.drawable.ic_notification_bus)
                    .setContentTitle("Não se esqueça de apanhar o transporte!")
                    .setContentText("Clique aqui para abrir a aplicação e ver os horários!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                println("Entrou no Notification Manager Compat")
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }
        } else if (ACTION2 == action) {
            // do some other stuff...
        } else {
            throw IllegalArgumentException("Unsupported action: " + action);
        }

    }
}