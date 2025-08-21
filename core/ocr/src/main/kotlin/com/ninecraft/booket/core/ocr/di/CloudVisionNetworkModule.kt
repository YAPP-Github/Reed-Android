package com.ninecraft.booket.core.ocr.di

import com.ninecraft.booket.core.ocr.BuildConfig
import com.ninecraft.booket.core.ocr.service.CloudVisionService
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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://vision.googleapis.com/"
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

@Module
@InstallIn(SingletonComponent::class)
object CloudVisionNetworkModule {

    @Provides
    @Singleton
    @CloudVisionOkHttp
    fun provideOkHttp(): OkHttpClient {
        val log = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(log)
            .connectTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .readTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .writeTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
            .build()
    }

    @Provides
    @Singleton
    @CloudVisionRetrofit
    fun provideRetrofit(
        @CloudVisionOkHttp okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideVisionApi(@CloudVisionRetrofit retrofit: Retrofit): CloudVisionService =
        retrofit.create(CloudVisionService::class.java)
}
