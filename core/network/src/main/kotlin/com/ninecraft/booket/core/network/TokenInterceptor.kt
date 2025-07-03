package com.ninecraft.booket.core.network

import com.ninecraft.booket.core.datastore.api.datasource.TokenPreferencesDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class TokenInterceptor @Inject constructor(
    @Suppress("unused")
    private val tokenDataSource: TokenPreferencesDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { tokenDataSource.getAccessToken() }

        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build(),
        )
    }
}
