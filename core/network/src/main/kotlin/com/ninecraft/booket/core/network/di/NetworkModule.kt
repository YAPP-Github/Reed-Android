package com.ninecraft.booket.core.network.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ninecraft.booket.core.network.BuildConfig
import com.ninecraft.booket.core.network.TokenInterceptor
import com.ninecraft.booket.core.network.TokenAuthenticator
import com.ninecraft.booket.core.network.service.AuthService
import com.ninecraft.booket.core.network.service.NoAuthService
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val MaxTimeoutMillis = 15_000L

private val jsonRule = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}

private val jsonConverterFactory = jsonRule.asConverterFactory("application/json".toMediaType())

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Singleton
    @Provides
    internal fun provideNetworkLogAdapter(): AndroidLogAdapter {
        val networkFormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) // 스레드 정보 제거
            .methodCount(0) // 메서드 스택 제거
            .methodOffset(0) // 오프셋 제거
            .tag("NETWORK") // API 호출 전용 태그
            .build()

        return AndroidLogAdapter(networkFormatStrategy)
    }

    @Singleton
    @Provides
    internal fun provideHttpLoggingInterceptor(
        networkLogAdapter: AndroidLogAdapter,
    ): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            if (message.isNotBlank()) {
                networkLogAdapter.log(Log.DEBUG, null, message)
            }
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @AuthOkHttpClient
    @Singleton
    @Provides
    internal fun provideAuthOkHttpClient(
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

    @NoAuthOkHttpClient
    @Singleton
    @Provides
    internal fun provideNoAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .readTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .writeTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @AuthRetrofit
    @Singleton
    @Provides
    internal fun provideAuthRetrofit(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @NoAuthRetrofit
    @Singleton
    @Provides
    internal fun provideNoAuthRetrofit(
        @NoAuthOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideAuthService(
        @AuthRetrofit retrofit: Retrofit,
    ): AuthService {
        return retrofit.create()
    }

    @Singleton
    @Provides
    internal fun provideNoAuthService(
        @NoAuthRetrofit retrofit: Retrofit,
    ): NoAuthService {
        return retrofit.create()
    }
}
