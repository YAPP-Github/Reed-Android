package com.ninecraft.booket.core.datastore.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ninecraft.booket.core.datastore.api.datasource.RecentSearchDataSource
import com.ninecraft.booket.core.datastore.api.datasource.TokenPreferencesDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultRecentSearchDataSource
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultTokenPreferencesDataSource
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
    private const val TOKEN_DATASTORE_NAME = "TOKENS_PREFERENCES"
    private val Context.dataStore by preferencesDataStore(name = TOKEN_DATASTORE_NAME)

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.dataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreBindModule {

    @Binds
    @Singleton
    abstract fun bindTokenPreferencesDataSource(
        defaultTokenPreferencesDataSource: DefaultTokenPreferencesDataSource,
    ): TokenPreferencesDataSource

    @Binds
    @Singleton
    abstract fun bindRecentSearchDataSource(
        defaultRecentSearchDataSource: DefaultRecentSearchDataSource,
    ): RecentSearchDataSource
}
