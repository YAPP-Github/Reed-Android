package com.ninecraft.booket.core.datastore.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ninecraft.booket.core.datastore.api.datasource.BookRecentSearchDataSource
import com.ninecraft.booket.core.datastore.api.datasource.LibraryRecentSearchDataSource
import com.ninecraft.booket.core.datastore.api.datasource.OnboardingDataSource
import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultLibraryRecentSearchDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultOnboardingDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultBookRecentSearchDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultTokenDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val TOKEN_DATASTORE_NAME = "TOKENS_DATASTORE"
    private const val BOOK_RECENT_SEARCH_DATASTORE_NAME = "BOOK_RECENT_SEARCH_DATASTORE"
    private const val LIBRARY_RECENT_SEARCH_DATASTORE_NAME = "LIBRARY_RECENT_SEARCH_DATASTORE"
    private const val ONBOARDING_DATASTORE_NAME = "ONBOARDING_DATASTORE"

    private val Context.tokenDataStore by preferencesDataStore(name = TOKEN_DATASTORE_NAME)
    private val Context.bookRecentSearchDataStore by preferencesDataStore(name = BOOK_RECENT_SEARCH_DATASTORE_NAME)
    private val Context.libraryRecentSearchDataStore by preferencesDataStore(name = LIBRARY_RECENT_SEARCH_DATASTORE_NAME)
    private val Context.onboardingDataStore by preferencesDataStore(name = ONBOARDING_DATASTORE_NAME)

    @TokenDataStore
    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.tokenDataStore

    @BookRecentSearchDataStore
    @Provides
    @Singleton
    fun provideBookRecentSearchDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.bookRecentSearchDataStore

    @LibraryRecentSearchDataStore
    @Provides
    @Singleton
    fun provideLibraryRecentSearchDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.libraryRecentSearchDataStore

    @OnboardingDataStore
    @Provides
    @Singleton
    fun provideOnboardingDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.onboardingDataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreBindModule {

    @Binds
    @Singleton
    abstract fun bindTokenDataSource(
        defaultTokenDataSource: DefaultTokenDataSource,
    ): TokenDataSource

    @Binds
    @Singleton
    abstract fun bindBookRecentSearchDataSource(
        defaultBookRecentSearchDataSource: DefaultBookRecentSearchDataSource,
    ): BookRecentSearchDataSource

    @Binds
    @Singleton
    abstract fun bindLibraryRecentSearchDataSource(
        defaultLibraryRecentSearchDataSource: DefaultLibraryRecentSearchDataSource,
    ): LibraryRecentSearchDataSource

    @Binds
    @Singleton
    abstract fun bindOnboardingDataSource(
        defaultOnboardingDataSource: DefaultOnboardingDataSource,
    ): OnboardingDataSource
}
