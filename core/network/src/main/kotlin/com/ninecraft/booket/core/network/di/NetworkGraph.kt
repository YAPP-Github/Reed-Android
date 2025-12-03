package com.ninecraft.booket.core.network.di

import android.util.Log
import com.ninecraft.booket.core.di.DataScope
import com.ninecraft.booket.core.network.BuildConfig
import com.ninecraft.booket.core.network.TokenAuthenticator
import com.ninecraft.booket.core.network.TokenInterceptor
import com.ninecraft.booket.core.network.service.ReedService
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

private const val MaxTimeoutMillis = 15_000L

private val jsonRule = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}

private val jsonConverterFactory = jsonRule.asConverterFactory("application/json".toMediaType())

private val FILTERED_HEADERS = setOf(
    "transfer-encoding",
    "connection",
    "x-content-type-options",
    "x-xss-protection",
    "cache-control",
    "pragma",
    "expires",
    "x-frame-options",
    "keep-alive",
    "server",
    "content-length",
)

@ContributesTo(DataScope::class)
interface NetworkGraph {

    @Provides
    fun provideNetworkLogAdapter(): AndroidLogAdapter {
        val networkFormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(0)
            .tag("NETWORK")
            .build()

        return AndroidLogAdapter(networkFormatStrategy)
    }

    @Provides
    fun provideHttpLoggingInterceptor(
        networkLogAdapter: AndroidLogAdapter,
    ): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message ->
            val shouldFilter = FILTERED_HEADERS.any { header ->
                message.lowercase().contains("$header:")
            }

            val isDuplicateContentType = message.lowercase().contains("content-type: application/json") &&
                !message.contains("charset")

            if (!shouldFilter && !isDuplicateContentType && message.isNotBlank()) {
                networkLogAdapter.log(Log.DEBUG, null, message)
            }
        }
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .readTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .writeTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .addInterceptor(tokenInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Provides
    fun provideReedService(
        retrofit: Retrofit,
    ): ReedService {
        return retrofit.create()
    }
}
