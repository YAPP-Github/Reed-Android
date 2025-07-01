package com.ninecraft.booket.core.network

import com.ninecraft.booket.core.datastore.datasource.TokenPreferencesDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class TokenInterceptor @Inject constructor(
    @Suppress("unused")
    private val tokenPreferencesDataSource: TokenPreferencesDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = ""

        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build(),
        )
    }
}
