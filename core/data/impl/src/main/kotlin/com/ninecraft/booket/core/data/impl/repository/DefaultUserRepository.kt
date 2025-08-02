package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.datastore.api.datasource.OnboardingDataSource
import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.model.AutoLoginState
import com.ninecraft.booket.core.network.service.ReedService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultUserRepository @Inject constructor(
    private val service: ReedService,
    private val onboardingDataSource: OnboardingDataSource,
    private val tokenDataSource: TokenDataSource,
) : UserRepository {
    override suspend fun getUserProfile() = runSuspendCatching {
        service.getUserProfile().toModel()
    }

    override val onboardingState = onboardingDataSource.onboardingState

    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        onboardingDataSource.setOnboardingCompleted(isCompleted)
    }

    override val autoLoginState: Flow<AutoLoginState> = tokenDataSource.accessToken
        .map { accessToken ->
            when {
                accessToken.isBlank() -> AutoLoginState.NOT_LOGGED_IN
                else -> AutoLoginState.LOGGED_IN
            }
        }
}
