package com.ninecraft.booket.core.network

import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.di.DataScope
import com.ninecraft.booket.core.network.request.RefreshTokenRequest
import com.ninecraft.booket.core.network.service.ReedService
import com.orhanobut.logger.Logger
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

@SingleIn(DataScope::class)
@Inject
class TokenAuthenticator(
    private val tokenDataSource: TokenDataSource,
    private val reedService: Lazy<ReedService>,
) : Authenticator {
    private val lock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        // 동시 401 응답 시 중복 refresh 방지 (refresh token rotation 대응)
        synchronized(lock) {
            val failedToken = response.request.header("Authorization")
                ?.removePrefix("Bearer ")
                .orEmpty()

            val currentToken = runBlocking { tokenDataSource.getAccessToken() }

            // 다른 요청이 이미 토큰을 갱신한 경우, 새 토큰으로 재시도만 수행
            if (failedToken != currentToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            return runBlocking {
                try {
                    val refreshToken = tokenDataSource.getRefreshToken()

                    if (refreshToken.isBlank()) {
                        Logger.d("No refresh token available")
                        tokenDataSource.clearTokens()
                        return@runBlocking null
                    }

                    val refreshResponse = reedService.value.refreshToken(RefreshTokenRequest(refreshToken))

                    tokenDataSource.apply {
                        setAccessToken(refreshResponse.accessToken)
                        setRefreshToken(refreshResponse.refreshToken)
                    }

                    Logger.d("Token refreshed successfully")

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                        .build()
                } catch (e: Exception) {
                    Logger.e(e, "Token refresh failed")
                    tokenDataSource.clearTokens()
                    return@runBlocking null
                }
            }
        }
    }
}
