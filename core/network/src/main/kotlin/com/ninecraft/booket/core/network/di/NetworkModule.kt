package com.ninecraft.booket.core.network.di

import android.util.Log
import com.orhanobut.logger.Logger
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
import com.ninecraft.booket.core.network.service.BooketService
import com.ninecraft.booket.core.network.service.LoginService
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
        networkLogAdapter: AndroidLogAdapter
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

    @TokenOkHttpClient
    @Singleton
    @Provides
    internal fun provideTokenOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .readTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .writeTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @DefaultOkHttpClient
    @Singleton
    @Provides
    internal fun provideDefaultOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .readTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .writeTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @TokenRetrofit
    @Singleton
    @Provides
    internal fun provideTokenRetrofit(
        @TokenOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @DefaultRetrofit
    @Singleton
    @Provides
    internal fun provideDefaultRetrofit(
        @DefaultOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideBooketService(
        @TokenRetrofit retrofit: Retrofit,
    ): BooketService {
        return retrofit.create()
    }

    @Singleton
    @Provides
    internal fun provideLoginService(
        @DefaultRetrofit retrofit: Retrofit,
    ): LoginService {
        return retrofit.create()
    }
}
