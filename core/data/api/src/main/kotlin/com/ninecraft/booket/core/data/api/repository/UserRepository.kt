package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.OnboardingState
import com.ninecraft.booket.core.model.TermsAgreementModel
import com.ninecraft.booket.core.model.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun agreeTerms(termsAgreed: Boolean): Result<TermsAgreementModel>

    suspend fun getUserProfile(): Result<UserProfileModel>

    val onboardingState: Flow<OnboardingState>

    suspend fun setOnboardingCompleted(isCompleted: Boolean)
}
