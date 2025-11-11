package com.ninecraft.booket.core.ocr.di

import com.ninecraft.booket.core.di.DataScope
import com.ninecraft.booket.core.ocr.BuildConfig
import com.ninecraft.booket.core.ocr.service.CloudVisionService
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
private const val CLOUD_VISION_BASE_URL = "https://vision.googleapis.com/"

private val jsonRule = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}

private val jsonConverterFactory = jsonRule.asConverterFactory("application/json".toMediaType())

@ContributesTo(DataScope::class)
interface OcrGraph {

    @CloudVisionOkHttp
    @Provides
    fun provideCloudVisionOkHttpClient(): OkHttpClient {
        val log = HttpLoggingInterceptor().apply {
            redactHeader("X-Goog-Api-Key")
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

    @CloudVisionRetrofit
    @Provides
    fun provideCloudVisionRetrofit(
        @CloudVisionOkHttp okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CLOUD_VISION_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Provides
    fun provideCloudVisionService(
        @CloudVisionRetrofit retrofit: Retrofit,
    ): CloudVisionService {
        return retrofit.create()
    }
}
