package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.datastore.api.datasource.TokenPreferencesDataSource
import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.service.AuthService
import com.ninecraft.booket.core.network.service.NoAuthService
import javax.inject.Inject

private const val KAKAO_PROVIDER_TYPE = "KAKAO"

internal class DefaultAuthRepository @Inject constructor(
    private val noAuthService: NoAuthService,
    private val authService: AuthService,
    private val tokenDatasource: TokenPreferencesDataSource,
) : AuthRepository {
    override suspend fun login(accessToken: String) = runSuspendCatching {
        val response = noAuthService.login(
            LoginRequest(
                providerType = KAKAO_PROVIDER_TYPE,
                oauthToken = accessToken,
            ),
        )
        saveTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun logout() = runSuspendCatching {
        authService.logout()
        clearTokens()
    }

    private suspend fun saveTokens(accessToken: String, refreshToken: String) {
        tokenDatasource.apply {
            setAccessToken(accessToken)
            setRefreshToken(refreshToken)
        }
    }

    private suspend fun clearTokens() {
        tokenDatasource.clearTokens()
    }
}
