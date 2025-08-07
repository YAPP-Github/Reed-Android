package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.datastore.api.datasource.OnboardingDataSource
import com.ninecraft.booket.core.network.request.TermsAgreementRequest
import com.ninecraft.booket.core.network.service.ReedService
import javax.inject.Inject

internal class DefaultUserRepository @Inject constructor(
    private val service: ReedService,
    private val onboardingDataSource: OnboardingDataSource,
) : UserRepository {
    override suspend fun agreeTerms(termsAgreed: Boolean) = runSuspendCatching {
        service.agreeTerms(TermsAgreementRequest(termsAgreed))
        Unit
    }

    override suspend fun getUserProfile() = runSuspendCatching {
        service.getUserProfile().toModel()
    }

    override val onboardingState = onboardingDataSource.onboardingState

    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        onboardingDataSource.setOnboardingCompleted(isCompleted)
    }
}
