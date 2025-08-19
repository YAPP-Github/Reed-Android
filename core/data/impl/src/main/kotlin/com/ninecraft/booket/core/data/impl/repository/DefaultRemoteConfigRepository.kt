package com.ninecraft.booket.core.data.impl.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.ninecraft.booket.core.data.api.repository.RemoteConfigRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultRemoteConfigRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) : RemoteConfigRepository {
    override suspend fun getLatestVersion(): Result<String> = suspendCancellableCoroutine { continuation ->
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val latestVersion = remoteConfig[KEY_LATEST_VERSION].asString()
                Logger.d("LatestVersion: $latestVersion")
                continuation.resume(Result.success(latestVersion))
            } else {
                Logger.e(task.exception, "getLatestVersion failed")
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error")))
            }
        }
    }

    companion object {
        private const val KEY_LATEST_VERSION = "LatestVersion"
    }
}
