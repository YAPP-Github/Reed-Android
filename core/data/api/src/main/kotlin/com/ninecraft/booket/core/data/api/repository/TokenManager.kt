package com.ninecraft.booket.core.data.api.repository

interface TokenManager {
    suspend fun getAccessToken(): String
    suspend fun getRefreshToken(): String
    suspend fun setAccessToken(token: String)
    suspend fun setRefreshToken(token: String)
    suspend fun clearTokens()
}
