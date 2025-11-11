package com.ninecraft.booket.core.data.impl.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ninecraft.booket.core.di.DataScope
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.data.api.repository.RemoteConfigRepository
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultAuthRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultBookRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultRecordRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultRemoteConfigRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultUserRepository
import com.ninecraft.booket.core.datastore.api.datasource.BookRecentSearchDataSource
import com.ninecraft.booket.core.datastore.api.datasource.LibraryRecentSearchDataSource
import com.ninecraft.booket.core.datastore.api.datasource.NotificationDataSource
import com.ninecraft.booket.core.datastore.api.datasource.OnboardingDataSource
import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultBookRecentSearchDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultLibraryRecentSearchDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultNotificationDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultOnboardingDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultTokenDataSource
import com.ninecraft.booket.core.datastore.impl.di.BookRecentSearchDataStore
import com.ninecraft.booket.core.datastore.impl.di.LibraryRecentSearchDataStore
import com.ninecraft.booket.core.datastore.impl.di.NotificationDataStore
import com.ninecraft.booket.core.datastore.impl.di.OnboardingDataStore
import com.ninecraft.booket.core.datastore.impl.di.TokenDataStore
import com.ninecraft.booket.core.network.BuildConfig
import com.ninecraft.booket.core.network.TokenAuthenticator
import com.ninecraft.booket.core.network.TokenInterceptor
import com.ninecraft.booket.core.network.service.ReedService
import com.ninecraft.booket.core.ocr.di.CloudVisionOkHttp
import com.ninecraft.booket.core.ocr.di.CloudVisionRetrofit
import com.ninecraft.booket.core.ocr.service.CloudVisionService
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import dev.zacsweers.metro.Binds
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

// DataStore constants and extensions
private const val TOKEN_DATASTORE_NAME = "TOKENS_DATASTORE"
private const val BOOK_RECENT_SEARCH_DATASTORE_NAME = "BOOK_RECENT_SEARCH_DATASTORE"
private const val LIBRARY_RECENT_SEARCH_DATASTORE_NAME = "LIBRARY_RECENT_SEARCH_DATASTORE"
private const val ONBOARDING_DATASTORE_NAME = "ONBOARDING_DATASTORE"
private const val NOTIFICATION_DATASTORE_NAME = "NOTIFICATION_DATASTORE"

private val Context.tokenDataStore by preferencesDataStore(name = TOKEN_DATASTORE_NAME)
private val Context.bookRecentSearchDataStore by preferencesDataStore(name = BOOK_RECENT_SEARCH_DATASTORE_NAME)
private val Context.libraryRecentSearchDataStore by preferencesDataStore(name = LIBRARY_RECENT_SEARCH_DATASTORE_NAME)
private val Context.onboardingDataStore by preferencesDataStore(name = ONBOARDING_DATASTORE_NAME)
private val Context.notificationDataStore by preferencesDataStore(name = NOTIFICATION_DATASTORE_NAME)

@ContributesTo(DataScope::class)
interface DataGraph {

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

    @CloudVisionOkHttp
    @Provides
    fun provideCloudVisionOkHttpClient(): OkHttpClient {
        val log = HttpLoggingInterceptor().apply {
            redactHeader("X-Goog-Api-Key")
            level = if (com.ninecraft.booket.core.ocr.BuildConfig.DEBUG) {
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

    @TokenDataStore
    @Provides
    fun provideTokenDataStore(
        context: Context,
    ): DataStore<Preferences> = context.tokenDataStore

    @BookRecentSearchDataStore
    @Provides
    fun provideBookRecentSearchDataStore(
        context: Context,
    ): DataStore<Preferences> = context.bookRecentSearchDataStore

    @LibraryRecentSearchDataStore
    @Provides
    fun provideLibraryRecentSearchDataStore(
        context: Context,
    ): DataStore<Preferences> = context.libraryRecentSearchDataStore

    @OnboardingDataStore
    @Provides
    fun provideOnboardingDataStore(
        context: Context,
    ): DataStore<Preferences> = context.onboardingDataStore

    @NotificationDataStore
    @Provides
    fun provideNotificationDataStore(
        context: Context,
    ): DataStore<Preferences> = context.notificationDataStore

    @Provides
    fun provideTokenDataSource(
        defaultTokenDataSource: DefaultTokenDataSource,
    ): TokenDataSource = defaultTokenDataSource

    @Provides
    fun provideBookRecentSearchDataSource(
        defaultBookRecentSearchDataSource: DefaultBookRecentSearchDataSource,
    ): BookRecentSearchDataSource = defaultBookRecentSearchDataSource

    @Provides
    fun provideLibraryRecentSearchDataSource(
        defaultLibraryRecentSearchDataSource: DefaultLibraryRecentSearchDataSource,
    ): LibraryRecentSearchDataSource = defaultLibraryRecentSearchDataSource

    @Provides
    fun provideOnboardingDataSource(
        defaultOnboardingDataSource: DefaultOnboardingDataSource,
    ): OnboardingDataSource = defaultOnboardingDataSource

    @Provides
    fun provideNotificationDataSource(
        defaultNotificationDataSource: DefaultNotificationDataSource,
    ): NotificationDataSource = defaultNotificationDataSource

    @Binds
    val DefaultAuthRepository.bindAuthRepository: AuthRepository

    @Binds
    val DefaultBookRepository.bindBookRepository: BookRepository

    @Binds
    val DefaultRecordRepository.bindRecordRepository: RecordRepository

    @Binds
    val DefaultRemoteConfigRepository.bindRemoteConfigRepository: RemoteConfigRepository

    @Binds
    val DefaultUserRepository.bindUserRepository: UserRepository
}