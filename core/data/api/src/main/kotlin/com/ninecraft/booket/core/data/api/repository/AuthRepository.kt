package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.AutoLoginState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(accessToken: String): Result<Unit>

    suspend fun logout(): Result<Unit>

    val autoLoginState: Flow<AutoLoginState>
}
