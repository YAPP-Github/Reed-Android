package com.ninecraft.booket.core.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ninecraft.booket.core.datastore.security.CryptoManager
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class TokenPreferencesDataSourceImpl @Inject constructor(
    @Named("token") private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager,
) : TokenPreferencesDataSource {
    override val accessToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { prefs ->
            prefs[ACCESS_TOKEN]?.let { encryptedToken ->
                try {
                    cryptoManager.decrypt(encryptedToken)
                } catch (e: Exception) {
                    Logger.e(e, "Failed to decrypt access token")
                    ""
                }
            }.orEmpty()
        }

    override val refreshToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { prefs ->
            prefs[REFRESH_TOKEN]?.let { encryptedToken ->
                try {
                    cryptoManager.decrypt(encryptedToken)
                } catch (e: Exception) {
                    Logger.e(e, "Failed to decrypt refresh token")
                    ""
                }
            }.orEmpty()
        }

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

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }
}
