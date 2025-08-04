package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.model.AutoLoginState
import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.request.TermsAgreementRequest
import com.ninecraft.booket.core.network.service.ReedService
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val KAKAO_PROVIDER_TYPE = "KAKAO"

internal class DefaultAuthRepository @Inject constructor(
    private val service: ReedService,
    private val tokenDataSource: TokenDataSource,
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

    override suspend fun agreeTerms(termsAgreed: Boolean) = runSuspendCatching {
        service.agreeTerms(TermsAgreementRequest(termsAgreed))
        Unit
    }

    private suspend fun saveTokens(accessToken: String, refreshToken: String) {
        tokenDataSource.apply {
            setAccessToken(accessToken)
            setRefreshToken(refreshToken)
        }
    }

    private suspend fun clearTokens() {
        tokenDataSource.clearTokens()
    }

    override val autoLoginState = tokenDataSource.accessToken
        .map { accessToken ->
            when {
                accessToken.isBlank() -> AutoLoginState.NOT_LOGGED_IN
                else -> AutoLoginState.LOGGED_IN
            }
        }
}
