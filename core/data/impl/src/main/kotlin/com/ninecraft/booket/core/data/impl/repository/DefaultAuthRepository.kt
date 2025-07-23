package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.service.ReedService
import javax.inject.Inject

private const val KAKAO_PROVIDER_TYPE = "KAKAO"

internal class DefaultAuthRepository @Inject constructor(
    private val service: ReedService,
    private val tokenDatasource: TokenDataSource,
) : AuthRepository {
    override suspend fun login(accessToken: String) = runSuspendCatching {
        val response = service.login(
            LoginRequest(
                providerType = KAKAO_PROVIDER_TYPE,
                oauthToken = accessToken,
            ),
        )
        saveTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun logout() = runSuspendCatching {
        service.logout()
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
