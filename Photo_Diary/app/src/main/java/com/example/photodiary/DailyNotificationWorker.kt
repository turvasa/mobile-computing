package com.example.photodiary

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.WorkManager


/**
 * Worker responsible for showing a daily notification to the user.
 * Uses [WorkManager] to execute the notifications in the foreground.
 *
 * @param context Application context.
 * @param workerParams Parameters passed by [WorkManager].
 */
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