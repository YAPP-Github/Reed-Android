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

    override val isNotificationEnabled = notificationDataSource.isNotificationEnabled

    override suspend fun setNotificationEnabled(isEnabled: Boolean) {
        notificationDataSource.setNotificationEnabled(isEnabled)
    }

    override suspend fun updateFcmToken(fcmToken: String) = runSuspendCatching {
        service.updateFcmToken(FcmTokenRequest(fcmToken)).toModel()
    }

    override suspend fun updateNotificationSettings(notificationEnabled: Boolean) = runSuspendCatching {
        service.updateNotificationSettings(NotificationSettingsRequest(notificationEnabled)).toModel()
    }
}
