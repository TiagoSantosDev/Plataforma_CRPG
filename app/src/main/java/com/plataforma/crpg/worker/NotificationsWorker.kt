package com.plataforma.crpg.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.plataforma.crpg.R
import com.plataforma.crpg.ui.MainActivity

class UploadWorker(appContext: Context, workerParams: WorkerParameters):
        Worker(appContext, workerParams) {
    override fun doWork(): Result {

        val intent = Intent(this, MainActivity::class.java).apply {
            println("Entrou no intent")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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

        with(NotificationManagerCompat.from(applicationContext)) {
            println("Entrou no Notification Manager Compat")
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}