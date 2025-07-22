package com.ninecraft.booket.core.network

import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class TokenInterceptor @Inject constructor(
    @Suppress("unused")
    private val tokenDataSource: TokenDataSource,
) : Interceptor {

    private val noAuthEndpoints = setOf(
        "api/v1/auth/signin",
        "api/v1/auth/refresh",
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        val isNoAuthEndpoint = noAuthEndpoints.any { url.contains(it) }

        return if (isNoAuthEndpoint) {
            chain.proceed(request)
        } else {
            val accessToken = runBlocking { tokenDataSource.getAccessToken() }
            chain.proceed(
                request.newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build(),
            )
        }
    }
}
