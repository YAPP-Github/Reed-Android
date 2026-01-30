package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.state.AutoLoginState
import com.ninecraft.booket.core.model.state.UserState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(
        providerType: String,
        token: String,
    ): Result<Unit>

    suspend fun logout(): Result<Unit>

    suspend fun withdraw(): Result<Unit>

    val autoLoginState: Flow<AutoLoginState>

    val userState: Flow<UserState>

    suspend fun getCurrentUserState(): UserState
}
