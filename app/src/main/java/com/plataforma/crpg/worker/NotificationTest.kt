package com.plataforma.crpg.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.plataforma.crpg.R


class NotificationTest(@NonNull context: Context?, @NonNull workerParams: WorkerParameters?) : Worker(context!!, workerParams!!) {
    @NonNull
    override fun doWork(): Result {
        showNotification("Background Task", "This notification is generated by workManager")
        return Result.success()
    }

    private fun showNotification(task: String, desc: String) {
        var notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                    NotificationChannel("101", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "101")
                .setContentTitle("Não se esqueça de apanhar o transporte!")
                .setContentText("Clique aqui para saber mais informações")
                .setSmallIcon(R.drawable.ic_notification_bus)

        notificationManager.notify(1, notificationBuilder.build())
    }

    companion object {
        private const val WORK_RESULT = "work_result"
    }
}