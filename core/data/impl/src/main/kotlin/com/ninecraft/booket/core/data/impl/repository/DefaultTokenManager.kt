package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.data.api.repository.TokenManager
import com.ninecraft.booket.core.datastore.api.datasource.TokenPreferencesDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DefaultTokenManager @Inject constructor(
    private val tokenPreferencesDataSource: TokenPreferencesDataSource
) : TokenManager {
    override suspend fun getAccessToken(): String = tokenPreferencesDataSource.accessToken.first()

    override suspend fun getRefreshToken(): String = tokenPreferencesDataSource.refreshToken.first()

    override suspend fun setAccessToken(token: String) {
       tokenPreferencesDataSource.setRefreshToken(token)
    }

    override suspend fun setRefreshToken(token: String) {
        tokenPreferencesDataSource.setRefreshToken(token)
    }

    override suspend fun clearTokens() {
        tokenPreferencesDataSource.clearTokens()
    }
}
