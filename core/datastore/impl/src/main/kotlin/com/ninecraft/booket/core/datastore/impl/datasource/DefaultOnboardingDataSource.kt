package com.ninecraft.booket.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.ninecraft.booket.core.datastore.api.datasource.OnboardingDataSource
import com.ninecraft.booket.core.datastore.impl.di.OnboardingDataStore
import com.ninecraft.booket.core.datastore.impl.util.handleIOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultOnboardingDataSource @Inject constructor(
    @OnboardingDataStore private val dataStore: DataStore<Preferences>,
) : OnboardingDataSource {
    override val isOnboardingCompleted: Flow<Boolean> = dataStore.data
        .handleIOException()
        .map { prefs ->
            prefs[IS_ONBOARDING_COMPLETED] ?: false
        }

    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_ONBOARDING_COMPLETED] = isCompleted
        }
    }

    companion object {
        private val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("IS_ONBOARDING_COMPLETED")
    }
}
