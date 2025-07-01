package com.ninecraft.booket.core.network

import com.ninecraft.booket.core.datastore.datasource.TokenPreferencesDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class TokenInterceptor @Inject constructor(
    private val tokenPreferencesDataSource: TokenPreferencesDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYmFmYzAwMS03MDUzLTRlYzQtYTJkNS03YWE4MmU0MWMyN2UiLCJpYXQiOjE3NTEzNzcwMTcsImV4cCI6MTc1MTM3ODgxNywidHlwZSI6ImFjY2VzcyJ9.MA8pYL1RRh5W0zHrGfps1Ko7frZ4lTbB17nRnbFu1jM_53ZBK90Mc4rKjRDprPyvhQWutrGzNdiAfOshiUrDKA"

        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build(),
        )
    }
}
