package com.ninecraft.booket.core.network

import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.network.request.RefreshTokenRequest
import com.ninecraft.booket.core.network.service.ReedService
import com.orhanobut.logger.Logger
import dev.zacsweers.metro.Inject
import com.ninecraft.booket.core.di.DataScope
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.Provider
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

@SingleIn(DataScope::class)
@Inject
class TokenAuthenticator(
    private val tokenDataSource: TokenDataSource,
    // private val serviceProvider: Provider<ReedService>,
    private val serviceLazy: Lazy<ReedService>,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            try {
                val refreshToken = tokenDataSource.getRefreshToken()

                if (refreshToken.isBlank()) {
                    Logger.d("TokenAuthenticator", "No refresh token available")
                    tokenDataSource.clearTokens()
                    return@runBlocking null
                }

                val refreshTokenRequest = RefreshTokenRequest(refreshToken)
                // val refreshResponse = serviceProvider().refreshToken(refreshTokenRequest)
                val refreshResponse = serviceLazy.value.refreshToken(refreshTokenRequest)

                tokenDataSource.apply {
                    setAccessToken(refreshResponse.accessToken)
                    setRefreshToken(refreshResponse.refreshToken)
                }

                Logger.d("TokenAuthenticator", "Token refreshed successfully")

                response.request.newBuilder()
                    .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                    .build()
            } catch (e: Exception) {
                Logger.e("TokenAuthenticator", e.message)
                tokenDataSource.clearTokens()

                // refresh token이 만료되었거나 잘못된 경우, 재시도하지 않음
                return@runBlocking null
            }
        }
    }
}
