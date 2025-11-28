package com.ninecraft.booket.core.data.impl.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.ninecraft.booket.core.common.utils.isUpdateRequired
import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.RemoteConfigRepository
import com.ninecraft.booket.core.data.impl.BuildConfig
import com.orhanobut.logger.Logger
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultRemoteConfigRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) : RemoteConfigRepository {
    override suspend fun getLatestVersion(): Result<String> = runSuspendCatching {
        remoteConfig.fetchAndActivate().await()
        val latestVersion = remoteConfig[KEY_LATEST_VERSION].asString()
        Logger.d("LatestVersion: $latestVersion")
        latestVersion
    }.onFailure { exception ->
        Logger.e(exception, "getLatestVersion failed")
    }

    override suspend fun shouldUpdate(): Result<Boolean> = runSuspendCatching {
        remoteConfig.fetchAndActivate().await()
        val minVersion = remoteConfig[KEY_MIN_VERSION].asString()
        val currentVersion = BuildConfig.APP_VERSION
        isUpdateRequired(currentVersion, minVersion)
    }.onFailure { exception ->
        Logger.e(exception, "shouldUpdate: getMinVersion failed")
    }

    companion object {
        private const val KEY_LATEST_VERSION = "LatestVersion"
        private const val KEY_MIN_VERSION = "MinVersion"
    }
}
