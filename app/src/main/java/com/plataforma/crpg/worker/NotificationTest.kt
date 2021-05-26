package com.plataforma.crpg.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.plataforma.crpg.R
import com.plataforma.crpg.ui.MainActivity


class NotificationTest(@NonNull context: Context?, @NonNull workerParams: WorkerParameters?) : Worker(context!!, workerParams!!) {
    @RequiresApi(Build.VERSION_CODES.O)
    @NonNull
    override fun doWork(): Result {
        val taskData: Data = inputData
        val taskDataString: String? = taskData.getString(MainActivity.MESSAGE_STATUS)
        showNotification("WorkManager", taskDataString ?: "Message has been Sent")
        val outputData: Data = Builder().putString(WORK_RESULT, "Jobs Finished").build()
        return Result.success(outputData)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(task: String, desc: String) {
        val manager: NotificationManager = ApplicationProvider.getApplicationContext<Context>().getSystemService<Any>(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_channel"
        val channelName = "task_name"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val builder: NotificationCompat.Builder = Builder(ApplicationProvider.getApplicationContext(), channelId)
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher)
        manager.notify(1, builder.build())
    }

    companion object {
        private const val WORK_RESULT = "work_result"
    }
}