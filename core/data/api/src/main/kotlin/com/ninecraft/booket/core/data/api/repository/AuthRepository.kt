package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.LoginModel

interface AuthRepository {
    suspend fun login(accessToken: String): Result<LoginModel>
    suspend fun logout(): Result<Unit>
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()
}
