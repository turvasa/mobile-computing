package com.example.photodiary

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DailyNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {

        NotificationHelper().showNotification(
            context = applicationContext,
            title = "Daily Diary dun",
            message = "Add today's Diary entry!"
        )

        return Result.success()
    }
}