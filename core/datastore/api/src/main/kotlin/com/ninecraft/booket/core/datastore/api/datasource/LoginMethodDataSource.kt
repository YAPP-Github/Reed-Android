package com.ninecraft.booket.core.datastore.api.datasource

import com.ninecraft.booket.core.model.LoginMethod
import kotlinx.coroutines.flow.Flow

interface LoginMethodDataSource {
    val recentLoginMethod: Flow<LoginMethod>
    suspend fun setRecentLoginMethod(loginMethod: LoginMethod)
    suspend fun clearRecentLoginMethod()
}