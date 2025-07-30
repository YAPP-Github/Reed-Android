package com.ninecraft.booket.core.datastore.api.datasource

import com.ninecraft.booket.core.model.OnboardingState
import kotlinx.coroutines.flow.Flow

interface OnboardingDataSource {
    val onboardingState: Flow<OnboardingState>
    suspend fun setOnboardingCompleted(isCompleted: Boolean)
}
