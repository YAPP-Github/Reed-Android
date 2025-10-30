package com.ninecraft.booket.core.data.impl.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.datastore.api.datasource.NotificationDataSource
import com.ninecraft.booket.core.datastore.api.datasource.OnboardingDataSource
import com.ninecraft.booket.core.network.request.FcmTokenRequest
import com.ninecraft.booket.core.network.request.NotificationSettingsRequest
import com.ninecraft.booket.core.network.request.TermsAgreementRequest
import com.ninecraft.booket.core.network.service.ReedService
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class DefaultUserRepository @Inject constructor(
    private val service: ReedService,
    private val onboardingDataSource: OnboardingDataSource,
    private val notificationDataSource: NotificationDataSource,
    private val firebaseMessaging: FirebaseMessaging,
) : UserRepository {
    override suspend fun agreeTerms(termsAgreed: Boolean) = runSuspendCatching {
        service.agreeTerms(TermsAgreementRequest(termsAgreed)).toModel()
    }

    override suspend fun getUserProfile() = runSuspendCatching {
        service.getUserProfile().toModel()
    }

    override val onboardingState = onboardingDataSource.onboardingState

    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        onboardingDataSource.setOnboardingCompleted(isCompleted)
    }

    override suspend fun syncFcmToken() = runSuspendCatching {
        val newToken = getRemoteFcmToken()
        val localToken = getLocalFcmToken()

        if (newToken == localToken) {
            Logger.d("Skip FCM token sync (already up-to-date)")
            return@runSuspendCatching
        }

        updateFcmToken(newToken)
        setFcmToken(newToken)
    }

    override suspend fun syncFcmToken(fcmToken: String): Result<Unit> = runSuspendCatching {
        updateFcmToken(fcmToken)
        setFcmToken(fcmToken)
    }

    override val isUserNotificationEnabled = notificationDataSource.isUserNotificationEnabled

    override suspend fun getUserNotificationEnabled(): Boolean = isUserNotificationEnabled.first()

    override suspend fun setUserNotificationEnabled(isEnabled: Boolean) {
        notificationDataSource.setUserNotificationEnabled(isEnabled)
    }

    override suspend fun getLastSyncedNotificationEnabled(): Boolean? =
        notificationDataSource.lastSyncedNotificationEnabled.firstOrNull()

    override suspend fun setLastNotificationSyncedEnabled(isEnabled: Boolean) {
        notificationDataSource.setLastSyncedNotificationEnabled(isEnabled)
    }

    override suspend fun updateNotificationSettings(notificationEnabled: Boolean) = runSuspendCatching {
        service.updateNotificationSettings(NotificationSettingsRequest(notificationEnabled)).toModel()
    }

    private suspend fun getRemoteFcmToken(): String {
        return try {
            firebaseMessaging.token.await()
        } catch (e: Exception) {
            Logger.e("Failed to fetch FCM token: ${e.message}")
            throw e
        }
    }

    private suspend fun getLocalFcmToken(): String = notificationDataSource.fcmToken.first()

    private suspend fun setFcmToken(fcmToken: String) {
        notificationDataSource.setFcmToken(fcmToken)
    }

    private suspend fun updateFcmToken(fcmToken: String) {
        service.updateFcmToken(FcmTokenRequest(fcmToken))
    }
}
