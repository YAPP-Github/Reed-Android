package com.ninecraft.booket.core.data.impl.di

import com.google.firebase.Firebase
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.installations.installations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.ninecraft.booket.core.data.impl.BuildConfig
import com.ninecraft.booket.core.di.DataScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(DataScope::class)
interface FirebaseGraph {

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            val configSettings by lazy {
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 60
                }
            }
            setConfigSettingsAsync(configSettings)
        }
    }

    @Provides
    fun provideFirebaseMessaging(): FirebaseMessaging = Firebase.messaging

    @Provides
    fun provideFirebaseInstallations(): FirebaseInstallations = Firebase.installations
}
