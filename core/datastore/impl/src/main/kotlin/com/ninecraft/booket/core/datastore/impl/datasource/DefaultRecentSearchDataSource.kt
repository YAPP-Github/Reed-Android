package com.ninecraft.booket.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ninecraft.booket.core.datastore.api.datasource.RecentSearchDataSource
import com.ninecraft.booket.core.datastore.impl.util.handleIOException
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DefaultRecentSearchDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : RecentSearchDataSource {
    @Suppress("TooGenericExceptionCaught")
    override val recentSearches: Flow<List<String>> = dataStore.data
        .handleIOException()
        .map { prefs ->
            prefs[RECENT_SEARCHES]?.let { jsonString ->
                try {
                    Json.decodeFromString<List<String>>(jsonString)
                } catch (e: Exception) {
                    Logger.e(e.toString())
                    emptyList()
                }
            } ?: emptyList()
        }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun addRecentSearch(query: String) {
        if (query.isBlank()) return

        dataStore.edit { prefs ->
            val currentSearches = prefs[RECENT_SEARCHES]?.let { jsonString ->
                try {
                    Json.decodeFromString<List<String>>(jsonString).toMutableList()
                } catch (e: Exception) {
                    Logger.e(e.toString())
                    mutableListOf()
                }
            } ?: mutableListOf()

            // 기존에 있으면 제거 (upsert 로직)
            currentSearches.remove(query)
            // 맨 앞에 추가 (가장 최근 검색어)
            currentSearches.add(0, query)

            // 최근 10개만 유지
            val limitedSearches = currentSearches.take(MAX_SEARCH_COUNT)
            prefs[RECENT_SEARCHES] = Json.encodeToString(limitedSearches)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun removeRecentSearch(query: String) {
        dataStore.edit { prefs ->
            val currentSearches = prefs[RECENT_SEARCHES]?.let { jsonString ->
                try {
                    Json.decodeFromString<List<String>>(jsonString).toMutableList()
                } catch (e: Exception) {
                    Logger.e(e.toString())
                    mutableListOf()
                }
            } ?: mutableListOf()

            currentSearches.remove(query)
            prefs[RECENT_SEARCHES] = Json.encodeToString(currentSearches)
        }
    }

    override suspend fun clearRecentSearches() {
        dataStore.edit { prefs ->
            prefs.remove(RECENT_SEARCHES)
        }
    }

    companion object {
        private val RECENT_SEARCHES = stringPreferencesKey("RECENT_SEARCHES")
        private const val MAX_SEARCH_COUNT = 10
    }
}
