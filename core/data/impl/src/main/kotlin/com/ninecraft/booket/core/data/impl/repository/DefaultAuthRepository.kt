package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.datastore.api.datasource.LoginMethodDataSource
import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.di.DataScope
import com.ninecraft.booket.core.model.LoginMethod
import com.ninecraft.booket.core.model.state.AutoLoginState
import com.ninecraft.booket.core.model.state.UserState
import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.service.ReedService
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.map

@SingleIn(DataScope::class)
@Inject
class DefaultAuthRepository(
    private val service: ReedService,
    private val tokenDataSource: TokenDataSource,
    private val loginMethodDataSource: LoginMethodDataSource,
) : AuthRepository {
    override suspend fun login(
        providerType: String,
        token: String,
    ) = runSuspendCatching {
        val response = service.login(
            LoginRequest(
                providerType = providerType,
                oauthToken = token,
            ),
        )
        saveTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun logout() = runSuspendCatching {
        service.logout()
        clearTokens()
    }

    override suspend fun withdraw() = runSuspendCatching {
        service.withdraw()
        clearTokens()
        clearRecentLoginMethod()
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

    override val recentLoginMethod = loginMethodDataSource.recentLoginMethod

    override suspend fun setRecentLoginMethod(loginMethod: LoginMethod) {
        loginMethodDataSource.setRecentLoginMethod(loginMethod)
    }

    override suspend fun clearRecentLoginMethod() {
        loginMethodDataSource.clearRecentLoginMethod()
    }
}
