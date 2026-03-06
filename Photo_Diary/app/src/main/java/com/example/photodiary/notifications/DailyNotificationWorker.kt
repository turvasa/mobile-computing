package com.example.photodiary.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Worker responsible for showing a daily notification to the user.
 * Uses [androidx.work.WorkManager] to execute the notifications in the foreground.
 *
 * @param context Application context.
 * @param workerParams Parameters passed by [androidx.work.WorkManager].
 */
class DailyNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {
        NotificationHelper().showNotification(
            context = applicationContext,
            title = "Daily Diary dun",
            message = "Add today's Diary entry!",
        )

        return Result.success()
    }
}