package com.ninecraft.booket.core.common.util

import com.orhanobut.logger.Logger

/**
 * 두 버전을 비교하는 함수
 *
 * @param version1 첫 번째 버전 (예: "1.2.3")
 * @param version2 두 번째 버전 (예: "1.1.0")
 * @return 양수면 version1 > version2, 음수면 version1 < version2, 0이면 같음
 *
 * 버전 형식: "메이저.마이너.패치" (예: 1.2.3)
 * 비교 순서: 메이저 → 마이너 → 패치 버전 순으로 비교
 */
fun compareVersions(version1: String, version2: String): Int {
    Logger.d("compareVersions: version1: $version1, version2: $version2")

    if (!Regex("""^\d+\.\d+\.\d+$""").matches(version1)) return 0
    if (!Regex("""^\d+\.\d+\.\d+$""").matches(version2)) return 0

    val v1 = version1.split('.').map { it.toInt() }
    val v2 = version2.split('.').map { it.toInt() }

    // 메이저 버전 비교
    if (v1[0] != v2[0]) return v1[0] - v2[0]

    // 마이너 버전 비교
    if (v1[1] != v2[1]) return v1[1] - v2[1]

    // 패치 버전 비교
    return v1[2] - v2[2]
}

/**
 * 현재 앱 버전이 최소 요구 버전보다 낮은지 확인하는 함수
 *
 * @param currentVersion 현재 앱의 버전 (예: "1.0.0")
 * @param minVersion 최소 요구 버전 (Firebase Remote Config에서 가져온 값)
 * @return true면 강제 업데이트 필요 (현재 버전 < 최소 요구 버전), false면 업데이트 불필요
 */
fun isUpdateRequired(currentVersion: String, minVersion: String): Boolean {
    return compareVersions(currentVersion, minVersion) < 0
}
