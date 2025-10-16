package com.ninecraft.booket.core.datastore.api.datasource

import kotlinx.coroutines.flow.Flow

interface NotificationDataSource {
    val isNotificationEnabled: Flow<Boolean>
    suspend fun setNotificationEnabled(isEnabled: Boolean)
}
