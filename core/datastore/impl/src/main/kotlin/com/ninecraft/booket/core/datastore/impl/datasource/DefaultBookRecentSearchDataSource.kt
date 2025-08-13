package com.ninecraft.booket.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ninecraft.booket.core.datastore.api.datasource.BookRecentSearchDataSource
import com.ninecraft.booket.core.datastore.impl.di.BookRecentSearchDataStore
import com.ninecraft.booket.core.datastore.impl.util.handleIOException
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DefaultBookRecentSearchDataSource @Inject constructor(
    @BookRecentSearchDataStore private val dataStore: DataStore<Preferences>,
) : BookRecentSearchDataSource {
    @Suppress("TooGenericExceptionCaught")
    override val recentSearches: Flow<List<String>> = dataStore.data
        .handleIOException()
        .map { prefs ->
            prefs[BOOK_RECENT_SEARCHES]?.let { jsonString ->
                try {
                    Json.decodeFromString<List<String>>(jsonString)
                } catch (e: SerializationException) {
                    Logger.e(e, "Failed to deserialize recent searches")
                    emptyList()
                } catch (e: Exception) {
                    Logger.e(e, "Unexpected error while reading recent searches")
                    emptyList()
                }
            } ?: emptyList()
        }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun addRecentSearch(query: String) {
        if (query.isBlank()) return

        dataStore.edit { prefs ->
            val currentSearches = prefs[BOOK_RECENT_SEARCHES]?.let { jsonString ->
                try {
                    Json.decodeFromString<List<String>>(jsonString).toMutableList()
                } catch (e: SerializationException) {
                    Logger.e(e, "Failed to deserialize recent searches for adding")
                    mutableListOf()
                } catch (e: Exception) {
                    Logger.e(e, "Unexpected error while adding recent search")
                    mutableListOf()
                }
            } ?: mutableListOf()

            // 기존에 있으면 제거 (upsert 로직)
            currentSearches.remove(query)
            // 맨 앞에 추가 (가장 최근 검색어)
            currentSearches.add(0, query)

            // 최근 10개만 유지
            val limitedSearches = currentSearches.take(MAX_SEARCH_COUNT)
            try {
                prefs[BOOK_RECENT_SEARCHES] = Json.encodeToString(limitedSearches)
            } catch (e: SerializationException) {
                Logger.e(e, "Failed to serialize recent searches")
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun removeRecentSearch(query: String) {
        dataStore.edit { prefs ->
            val currentSearches = prefs[BOOK_RECENT_SEARCHES]?.let { jsonString ->
                try {
                    Json.decodeFromString<List<String>>(jsonString).toMutableList()
                } catch (e: SerializationException) {
                    Logger.e(e, "Failed to deserialize recent searches for adding")
                    mutableListOf()
                } catch (e: Exception) {
                    Logger.e(e, "Unexpected error while adding recent search")
                    mutableListOf()
                }
            } ?: mutableListOf()

            currentSearches.remove(query)
            try {
                prefs[BOOK_RECENT_SEARCHES] = Json.encodeToString(currentSearches)
            } catch (e: SerializationException) {
                Logger.e(e, "Failed to serialize recent searches after removal")
            }
        }
    }

    override suspend fun clearRecentSearches() {
        dataStore.edit { prefs ->
            prefs.remove(BOOK_RECENT_SEARCHES)
        }
    }

    companion object {
        private val BOOK_RECENT_SEARCHES = stringPreferencesKey("BOOK_RECENT_SEARCHES")
        private const val MAX_SEARCH_COUNT = 10
    }
}
