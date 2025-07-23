package com.ninecraft.booket.core.datastore.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ninecraft.booket.core.datastore.api.datasource.RecentSearchDataSource
import com.ninecraft.booket.core.datastore.api.datasource.TokenDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultRecentSearchDataSource
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
    private const val RECENT_SEARCH_DATASTORE_NAME = "RECENT_SEARCH_DATASTORE"

    private val Context.tokenDataStore by preferencesDataStore(name = TOKEN_DATASTORE_NAME)
    private val Context.recentSearchDataStore by preferencesDataStore(name = RECENT_SEARCH_DATASTORE_NAME)

    @TokenDataStore
    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.tokenDataStore

    @RecentSearchDataStore
    @Provides
    @Singleton
    fun provideRecentSearchDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.recentSearchDataStore
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
    abstract fun bindRecentSearchDataSource(
        defaultRecentSearchDataSource: DefaultRecentSearchDataSource,
    ): RecentSearchDataSource
}
