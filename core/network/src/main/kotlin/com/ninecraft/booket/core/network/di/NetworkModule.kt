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
import com.ninecraft.booket.core.network.service.ReedService
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val MaxTimeoutMillis = 15_000L

private val jsonRule = Json {
    // 기본값도 JSON에 포함하여 직렬화
    encodeDefaults = true
    // JSON에 정의되지 않은 키는 무시 (역직렬화 시 에러 방지)
    ignoreUnknownKeys = true
    // JSON을 보기 좋게 들여쓰기하여 포맷팅
    prettyPrint = true
    // 엄격하지 않은 파싱 (따옴표 없는 키, 후행 쉼표 등 허용)
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
            val shouldFilter = FILTERED_HEADERS.any { header ->
                message.lowercase().contains("$header:")
            }

            val isDuplicateContentType = message.lowercase().contains("content-type: application/json") &&
                !message.contains("charset")

            if (!shouldFilter && !isDuplicateContentType && message.isNotBlank()) {
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

    @Singleton
    @Provides
    internal fun provideOkHttpClient(
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

    @Singleton
    @Provides
    internal fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideReedService(
        retrofit: Retrofit,
    ): ReedService {
        return retrofit.create()
    }
}
