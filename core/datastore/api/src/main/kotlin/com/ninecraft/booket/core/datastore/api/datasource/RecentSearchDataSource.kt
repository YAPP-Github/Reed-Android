package com.ninecraft.booket.core.datastore.api.datasource

import kotlinx.coroutines.flow.Flow

interface RecentSearchDataSource {
    val recentSearches: Flow<List<String>>
    suspend fun addRecentSearch(query: String)
    suspend fun removeRecentSearch(query: String)
    suspend fun clearRecentSearches()
}