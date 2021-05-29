package com.plataforma.crpg.notifications

import android.app.AlarmManager
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
import com.plataforma.crpg.utils.CustomDateUtils
import java.util.*

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

    fun createAlarmNotification(
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

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_notification_bus)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(autoCancel)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
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

        val publicUniqueID = System.currentTimeMillis().toInt() + 1
        val publicTransportsIntent = Intent(context, MainActivity::class.java)
        publicTransportsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        publicTransportsIntent.putExtra("id", "transport")
        publicTransportsIntent.putExtra("acao", "publico")
        val publicTransportsFragmentIntent = PendingIntent.getActivity(context, publicUniqueID,
                publicTransportsIntent, 0)

        val customUniqueID = System.currentTimeMillis().toInt() + 1
        val customTransportsIntent = Intent(context, MainActivity::class.java)
        customTransportsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        customTransportsIntent.putExtra("id", "transport")
        customTransportsIntent.putExtra("acao", "custom")
        val customTransportsFragmentIntent = PendingIntent.getActivity(context, customUniqueID,
                customTransportsIntent, 0)

        val mainUniqueID = System.currentTimeMillis().toInt() + 1
        val transportsIntent = Intent(context, MainActivity::class.java)
        transportsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        transportsIntent.putExtra("id", "transport")
        transportsIntent.putExtra("acao", "fixo")
        val transportsFragmentIntent = PendingIntent.getActivity(context, mainUniqueID,
                transportsIntent, 0)

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_notification_bus)
            setContentTitle(title)
            setContentText(message)
            .addAction(R.drawable.ic_notification_bus, "Transportes Públicos",
                    publicTransportsFragmentIntent)
            .addAction(R.drawable.ic_notification_bus, "Camioneta CRPG",
                    transportsFragmentIntent)
            .addAction(R.drawable.ic_notification_bus, "Os meus horários",
                    customTransportsFragmentIntent)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(autoCancel)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }

    fun createNewMedicationNotification(
            context: Context, title: String,
            message:
            String,
            autoCancel: Boolean,
    ){
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        val uniqueID = System.currentTimeMillis().toInt()

        val intent = Intent(context, MainActivity::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("id", "meds")
        val pendingIntent = PendingIntent.getActivity(context, uniqueID, intent, 0)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val reminderID = System.currentTimeMillis().toInt() + 1
        val remindIntent = Intent(context, MainActivity::class.java)
        //remindIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        remindIntent.putExtra("id", "meds")
        val remindPendingIntent = PendingIntent.getActivity(context, reminderID,
                remindIntent, 0)

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        //calendar.set(Calendar.HOUR_OF_DAY, CustomDateUtils.getCurrentHourInt())
        //calendar.set(Calendar.MINUTE, minute)
        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, (1000 * 60 * 5).toLong(), remindPendingIntent)
        //alarmMgr.set(AlarmManager.RTC_WAKEUP, (1000 * 60 * 2).toLong(), remindPendingIntent)

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.medicacao)
            setContentTitle(title)
            setContentText(message)
                    .addAction(R.drawable.medicacao, "Sim",
                           pendingIntent)
                    .addAction(R.drawable.medicacao, "Relembrar",
                            remindPendingIntent)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(autoCancel)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1005, notificationBuilder.build())
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