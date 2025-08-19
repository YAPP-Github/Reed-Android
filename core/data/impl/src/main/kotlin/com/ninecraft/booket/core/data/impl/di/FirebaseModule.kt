package com.ninecraft.booket.core.data.impl.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object FirebaseModule {
    @Singleton
    @Provides
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            val configSettings by lazy {
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 60
                }
            }
            setConfigSettingsAsync(configSettings)
        }
    }
}
