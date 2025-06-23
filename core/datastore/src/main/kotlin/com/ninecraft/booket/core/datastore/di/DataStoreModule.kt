package com.ninecraft.booket.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ninecraft.booket.core.datastore.datasource.TokenPreferencesDataSource
import com.ninecraft.booket.core.datastore.datasource.TokenPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val TOKEN_DATASTORE_NAME = "TOKENS_PREFERENCES"
    private val Context.dataStore by preferencesDataStore(name = TOKEN_DATASTORE_NAME)

    @Provides
    @Singleton
    @Named("token")
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
        tokenPreferencesDataSourceImpl: TokenPreferencesDataSourceImpl
    ): TokenPreferencesDataSource
}
