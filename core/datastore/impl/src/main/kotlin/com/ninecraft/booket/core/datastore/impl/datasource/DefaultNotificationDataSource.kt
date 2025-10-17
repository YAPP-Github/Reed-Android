package com.ninecraft.booket.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.ninecraft.booket.core.datastore.api.datasource.NotificationDataSource
import com.ninecraft.booket.core.datastore.impl.di.NotificationDataStore
import com.ninecraft.booket.core.datastore.impl.util.handleIOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultNotificationDataSource @Inject constructor(
    @NotificationDataStore private val dataStore: DataStore<Preferences>,
) : NotificationDataSource {
    override val isNotificationEnabled: Flow<Boolean> = dataStore.data
        .handleIOException()
        .map { prefs ->
            prefs[NOTIFICATION_ENABLED] ?: true
        }

    override suspend fun setNotificationEnabled(isEnabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[NOTIFICATION_ENABLED] = isEnabled
        }
    }

    companion object Companion {
        private val NOTIFICATION_ENABLED = booleanPreferencesKey("NOTIFICATION_ENABLED")
    }
}
