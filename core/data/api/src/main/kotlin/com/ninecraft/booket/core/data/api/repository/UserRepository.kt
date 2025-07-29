package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserProfile(): Result<UserProfileModel>

    val isOnboardingCompleted: Flow<Boolean>

    suspend fun setOnboardingCompleted(isCompleted: Boolean)
}
