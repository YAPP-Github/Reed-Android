package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.datastore.api.datasource.NotificationDataSource
import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.model.AutoLoginState
import com.ninecraft.booket.core.model.UserState
import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.service.ReedService
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val KAKAO_PROVIDER_TYPE = "KAKAO"

internal class DefaultAuthRepository @Inject constructor(
    private val service: ReedService,
    private val tokenDataSource: TokenDataSource,
    private val notificationDataSource: NotificationDataSource,
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
        clearNotificationDataStore()
    }

    override suspend fun withdraw() = runSuspendCatching {
        service.withdraw()
        clearTokens()
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
            if (accessToken.isBlank()) AutoLoginState.NOT_LOGGED_IN else AutoLoginState.LOGGED_IN
        }

    override val userState = tokenDataSource.accessToken
        .map { accessToken ->
            if (accessToken.isBlank()) UserState.Guest else UserState.LoggedIn
        }

    override suspend fun getCurrentUserState(): UserState {
        val accessToken = tokenDataSource.getAccessToken()
        return if (accessToken.isBlank()) UserState.Guest else UserState.LoggedIn
    }

    private suspend fun clearNotificationDataStore() {
        notificationDataSource.clearNotificationDataStore()
    }
}
