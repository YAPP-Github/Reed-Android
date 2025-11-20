package com.ninecraft.booket.core.datastore.api.datasource

import kotlinx.coroutines.flow.Flow

interface NotificationDataSource {
    val isUserNotificationEnabled: Flow<Boolean>
    suspend fun setUserNotificationEnabled(isEnabled: Boolean)

    val lastSyncedNotificationEnabled: Flow<Boolean?>
    suspend fun setLastSyncedNotificationEnabled(isEnabled: Boolean)

    suspend fun clearNotificationDataStore()
}
