package com.ninecraft.booket.core.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ninecraft.booket.core.datastore.security.CryptoManager
import com.ninecraft.booket.core.datastore.util.handleIOException
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.GeneralSecurityException
import javax.inject.Inject
import javax.inject.Named

class DefaultTokenPreferencesDataSource @Inject constructor(
    @Named("token") private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager,
) : TokenPreferencesDataSource {
    override val accessToken: Flow<String> = decryptStringFlow(ACCESS_TOKEN)
    override val refreshToken: Flow<String> = decryptStringFlow(REFRESH_TOKEN)

    override suspend fun setAccessToken(accessToken: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = cryptoManager.encrypt(accessToken)
        }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN] = cryptoManager.encrypt(refreshToken)
        }
    }

    override suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
        }
    }

    private fun decryptStringFlow(
        key: Preferences.Key<String>,
    ): Flow<String> = dataStore.data
        .handleIOException()
        .map { prefs ->
            prefs[key]?.let {
                try {
                    cryptoManager.decrypt(it)
                } catch (e: GeneralSecurityException) {
                    Logger.e(e, "Failed to decrypt token")
                    ""
                }
            }.orEmpty()
        }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }
}
