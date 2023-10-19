package com.example.tracker.models_impl.bg.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tracker.models.bg.work.WorkScheduler

class UploadWorkScheduler(private var context: Context) : WorkScheduler {

    override fun scheduleSync() {
        val requestSendLocation = OneTimeWorkRequestBuilder<UploadLocationsWork>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()
        WorkManager.getInstance(context).enqueue(requestSendLocation)
    }

}
