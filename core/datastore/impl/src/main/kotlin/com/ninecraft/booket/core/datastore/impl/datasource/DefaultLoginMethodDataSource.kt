package com.ninecraft.booket.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ninecraft.booket.core.datastore.api.datasource.LoginMethodDataSource
import com.ninecraft.booket.core.datastore.impl.di.LoginMethodDataStore
import com.ninecraft.booket.core.datastore.impl.util.handleIOException
import com.ninecraft.booket.core.di.DataScope
import com.ninecraft.booket.core.model.LoginMethod
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@SingleIn(DataScope::class)
@Inject
class DefaultLoginMethodDataSource(
    @LoginMethodDataStore private val dataStore: DataStore<Preferences>,
) : LoginMethodDataSource {
    override val recentLoginMethod: Flow<LoginMethod> = dataStore.data
        .handleIOException()
        .map { prefs ->
            val method = prefs[RECENT_LOGIN_METHOD]
            when (method) {
                "KAKAO" -> LoginMethod.KAKAO
                "GOOGLE" -> LoginMethod.GOOGLE
                else -> LoginMethod.NONE
            }
        }

    override suspend fun setRecentLoginMethod(loginMethod: LoginMethod) {
        dataStore.edit { prefs ->
            prefs[RECENT_LOGIN_METHOD] = loginMethod.name
        }
    }

    override suspend fun clearRecentLoginMethod() {
        dataStore.edit { prefs ->
            prefs.remove(RECENT_LOGIN_METHOD)
        }
    }

    companion object {
        private val RECENT_LOGIN_METHOD = stringPreferencesKey("RECENT_LOGIN_METHOD")
    }
}
