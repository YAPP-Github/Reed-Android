package com.ninecraft.booket.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ninecraft.booket.core.datastore.api.datasource.NotificationDataSource
import com.ninecraft.booket.core.datastore.impl.di.NotificationDataStore
import com.ninecraft.booket.core.datastore.impl.util.handleIOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultNotificationDataSource @Inject constructor(
    @NotificationDataStore private val dataStore: DataStore<Preferences>,
) : NotificationDataSource {
    override val fcmToken: Flow<String> = dataStore.data
        .handleIOException()
        .map { prefs ->
            prefs[FCM_TOKEN] ?: ""
        }

    override suspend fun setFcmToken(fcmToken: String) {
        dataStore.edit { prefs ->
            prefs[FCM_TOKEN] = fcmToken
        }
    }

    override val isUserNotificationEnabled: Flow<Boolean> = dataStore.data
        .handleIOException()
        .map { prefs ->
            prefs[USER_NOTIFICATION_ENABLED] ?: true
        }

    override suspend fun setUserNotificationEnabled(isEnabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[USER_NOTIFICATION_ENABLED] = isEnabled
        }
    }

    override val lastSyncedNotificationEnabled: Flow<Boolean?> = dataStore.data
        .handleIOException()
        .map { prefs ->
            prefs[LAST_SYNCED_NOTIFICATION_ENABLED]
        }

    override suspend fun setLastSyncedNotificationEnabled(isEnabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[LAST_SYNCED_NOTIFICATION_ENABLED] = isEnabled
        }
    }

    companion object Companion {
        private val FCM_TOKEN = stringPreferencesKey("FCM_TOKEN")
        private val USER_NOTIFICATION_ENABLED = booleanPreferencesKey("USER_NOTIFICATION_ENABLED")
        private val LAST_SYNCED_NOTIFICATION_ENABLED = booleanPreferencesKey("LAST_SYNCED_NOTIFICATION_ENABLED")
    }
}
