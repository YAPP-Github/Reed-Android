package com.ninecraft.booket.core.datastore.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
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
import com.ninecraft.booket.core.di.ApplicationContext
import com.ninecraft.booket.core.di.DataScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

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
interface DataStoreGraph {

    @TokenDataStore
    @Provides
    fun provideTokenDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.tokenDataStore

    @BookRecentSearchDataStore
    @Provides
    fun provideBookRecentSearchDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.bookRecentSearchDataStore

    @LibraryRecentSearchDataStore
    @Provides
    fun provideLibraryRecentSearchDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.libraryRecentSearchDataStore

    @OnboardingDataStore
    @Provides
    fun provideOnboardingDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.onboardingDataStore

    @NotificationDataStore
    @Provides
    fun provideNotificationDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.notificationDataStore

    @Binds
    val DefaultTokenDataSource.bind: TokenDataSource

    @Binds
    val DefaultBookRecentSearchDataSource.bind: BookRecentSearchDataSource

    @Binds
    val DefaultLibraryRecentSearchDataSource.bind: LibraryRecentSearchDataSource

    @Binds
    val DefaultOnboardingDataSource.bind: OnboardingDataSource

    @Binds
    val DefaultNotificationDataSource.bind: NotificationDataSource
}
