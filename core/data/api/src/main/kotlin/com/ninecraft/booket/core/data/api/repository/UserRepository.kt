package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.state.OnboardingState
import com.ninecraft.booket.core.model.TermsAgreementModel
import com.ninecraft.booket.core.model.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun agreeTerms(termsAgreed: Boolean): Result<TermsAgreementModel>

    suspend fun getUserProfile(): Result<UserProfileModel>

    val onboardingState: Flow<OnboardingState>

    suspend fun setOnboardingCompleted(isCompleted: Boolean)

    suspend fun syncFcmToken(): Result<Unit>

    suspend fun syncFcmToken(fcmToken: String): Result<Unit>

    val isUserNotificationEnabled: Flow<Boolean>

    suspend fun getUserNotificationEnabled(): Boolean

    suspend fun setUserNotificationEnabled(isEnabled: Boolean)

    suspend fun getLastSyncedNotificationEnabled(): Boolean?

    suspend fun setLastNotificationSyncedEnabled(isEnabled: Boolean)

    suspend fun updateNotificationSettings(notificationEnabled: Boolean): Result<UserProfileModel>

    suspend fun resetNotificationData()
}
