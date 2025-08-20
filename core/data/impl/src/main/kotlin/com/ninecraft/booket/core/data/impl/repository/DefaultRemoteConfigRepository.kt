package com.ninecraft.booket.core.data.impl.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.ninecraft.booket.core.data.api.repository.RemoteConfigRepository
import com.ninecraft.booket.core.data.impl.BuildConfig
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

    override suspend fun shouldUpdate(): Result<Boolean> = suspendCancellableCoroutine { continuation ->
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val minVersion = remoteConfig[KEY_MIN_VERSION].asString()
                val currentVersion = BuildConfig.APP_VERSION
                continuation.resume(Result.success(checkMinVersion(currentVersion, minVersion)))
            } else {
                Logger.e(task.exception, "shouldUpdate: getMinVersion failed")
                continuation.resume(Result.failure(task.exception ?: Exception("Unknown error")))
            }
        }
    }

    /**
     * 현재 앱 버전이 최소 요구 버전보다 낮은지 확인하는 함수
     *
     * @param currentVersion 현재 앱의 버전 (예: "1.0.0")
     * @param minVersion 최소 요구 버전 (Firebase Remote Config에서 가져온 값)
     * @return true면 강제 업데이트 필요 (현재 버전 < 최소 요구 버전), false면 업데이트 불필요
     *
     * 버전 형식: "메이저.마이너.패치" (예: 1.2.3)
     * 비교 순서: 메이저 → 마이너 → 패치 버전 순으로 비교
     */
    private fun checkMinVersion(currentVersion: String, minVersion: String): Boolean {
        Logger.d("checkMinVersion: current: $currentVersion, min: $minVersion")
        if (!Regex("""^\d+\.\d+\.\d+$""").matches(currentVersion)) return false
        if (!Regex("""^\d+\.\d+\.\d+$""").matches(minVersion)) return false

        val current = currentVersion.split('.').map { it.toInt() }
        val min = minVersion.split('.').map { it.toInt() }

        // 메이저 버전 비교
        if (current[0] != min[0]) return current[0] < min[0]

        // 마이너 버전 비교
        if (current[1] != min[1]) return current[1] < min[1]

        // 패치 버전 비교
        return current[2] < min[2]
    }

    companion object {
        private const val KEY_LATEST_VERSION = "LatestVersion"
        private const val KEY_MIN_VERSION = "MinVersion"
    }
}
