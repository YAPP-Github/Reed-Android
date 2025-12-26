package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.AutoLoginState
import com.ninecraft.booket.core.model.LoginMethod
import com.ninecraft.booket.core.model.UserState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(accessToken: String): Result<Unit>

    suspend fun logout(): Result<Unit>

    suspend fun withdraw(): Result<Unit>

    val autoLoginState: Flow<AutoLoginState>

    val userState: Flow<UserState>

    suspend fun getCurrentUserState(): UserState

    val recentLoginMethod: Flow<LoginMethod>

    suspend fun setRecentLoginMethod(loginMethod: LoginMethod)

    suspend fun clearRecentLoginMethod()
}
