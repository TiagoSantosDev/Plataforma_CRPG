package com.plataforma.crpg.notifications

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

object NotificationsManager {

    fun createNewNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNewTransportsNotification(
            context: Context, title: String,
            message:
            String,
            autoCancel: Boolean,
    ){
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        val uniqueID = System.currentTimeMillis().toInt()

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("id", "transport")
        val pendingIntent = PendingIntent.getActivity(context, uniqueID, intent, 0)

        val newUniqueID = System.currentTimeMillis().toInt() + 1
        val publicTransportsIntent = Intent(context, MainActivity::class.java)
        publicTransportsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        publicTransportsIntent.putExtra("id","transport")
        publicTransportsIntent.putExtra("acao","publico")
        val openTransportsFragmentIntent = PendingIntent.getActivity(context, newUniqueID, publicTransportsIntent, 0)

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_notification_bus)
            setContentTitle(title)
            setContentText(message)
            setStyle(NotificationCompat.BigTextStyle().bigText("Teste"))
            .addAction(R.drawable.ic_notification_bus, "Abrir Transportes Públicos",
                    openTransportsFragmentIntent)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(autoCancel)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }

    fun createNewTestNotification(
            context: Context, title: String,
            message:
            String,
            autoCancel: Boolean,
    ){
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_notification_bus)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(autoCancel)

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("id", "transport")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


            val uniqueID = System.currentTimeMillis().toInt()
            val pendingIntent = PendingIntent.getActivity(context, uniqueID, intent, 0)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1002, notificationBuilder.build())

    }

    fun createNewMealNotification(
            context: Context, title: String,
            message:
            String,
            autoCancel: Boolean,
    ){
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_notification_meal)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(autoCancel)

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("id", "meal")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            val uniqueID = System.currentTimeMillis().toInt()
            val pendingIntent = PendingIntent.getActivity(context, uniqueID, intent, 0)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1003, notificationBuilder.build())

    }


}
/*
 fun createNotificationForPet(context: Context) {

     // create the pet notification
     val notificationBuilder = buildNotificationForPet(context, reminderData)

     // add an action to the pet notification
     val administerPendingIntent = createPendingIntentForAction(context, reminderData)
     notificationBuilder.addAction(R.drawable.baseline_done_black_24, context.getString(R.string.administer), administerPendingIntent)

     // call notify for both the group and the pet notification
     val notificationManager = NotificationManagerCompat.from(context)
     notificationManager.notify(reminderData.id, notificationBuilder.build())
 }
 */

/*val fragment: Fragment = TransportsSelectionFragment()
                val fragmentManager: FragmentManager = context.supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()*/

//val intent = Intent(context, MainActivity::class.java).apply {
//flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//putExtra("fragment", "transports")
//}