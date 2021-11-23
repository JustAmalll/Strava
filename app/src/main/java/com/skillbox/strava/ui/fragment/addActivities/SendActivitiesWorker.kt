package com.skillbox.strava.ui.fragment.addActivities

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.shared_model.network.ActivityType
import dagger.assisted.*
import kotlinx.coroutines.*
import timber.log.Timber

@HiltWorker
class SendActivitiesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: AthleteRepository
) : CoroutineWorker(appContext, params) {

    @SuppressLint("LogNotTimber")
    override suspend fun doWork(): Result {
        val date = inputData.getString(MODEL_DATE)
        val type = inputData.getString(MODEL_TYPE)
        val name = inputData.getString(MODEL_NAME)
        val desc = inputData.getString(MODEL_DESC)
        val time = inputData.getString(MODEL_TIME)
        val dist = inputData.getString(MODEL_DIST)

        if (date.isNullOrEmpty() || type.isNullOrEmpty() || name.isNullOrEmpty()
            || time.isNullOrEmpty() || dist.isNullOrEmpty()
        ) return Result.failure()
        return withContext(Dispatchers.IO) {
            try {
                val isSuccess = repository.postActivities(
                    name,
                    ActivityType.valueOf(type),
                    date,
                    time.toInt(),
                    desc,
                    dist.toFloat()
                ) {}!!
                return@withContext if (isSuccess) {
                    Log.d("SendActivitiesWorker", "Запись успешно добавлена")
                    Result.success()
                } else {
                    Log.e("SendActivitiesWorker", "Ошибка добавления записи")
                    Result.failure()
                }
            } catch (t: Throwable) {
                Timber.e(t)
                Result.retry()
            }
        }
    }

    companion object {
        const val MODEL_NAME = "model_name"
        const val MODEL_TYPE = "model_type"
        const val MODEL_DIST = "model_distance"
        const val MODEL_DESC = "model_description"
        const val MODEL_TIME = "model_time"
        const val MODEL_DATE = "model_date"
    }
}