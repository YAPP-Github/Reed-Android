package com.ninecraft.booket.di

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import com.ninecraft.booket.core.di.DataScope
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.installations.installations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides

@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
)
interface AppGraph {

    @Provides
    fun provideApplicationContext(application: Application): Context = application

    @Multibinds(allowEmpty = true)
    val presenterFactories: Set<Presenter.Factory>

    @Multibinds(allowEmpty = true)
    val uiFactories: Set<Ui.Factory>

    @Multibinds(allowEmpty = true)
    val activityProviders: Map<kotlin.reflect.KClass<out Activity>, dev.zacsweers.metro.Provider<Activity>>

    @Multibinds(allowEmpty = true)
    val serviceProviders: Map<kotlin.reflect.KClass<out Service>, dev.zacsweers.metro.Provider<Service>>

    @Provides
    fun provideCircuit(
        presenterFactories: Set<Presenter.Factory>,
        uiFactories: Set<Ui.Factory>,
    ): Circuit {
        return Circuit.Builder()
            .addPresenterFactories(presenterFactories)
            .addUiFactories(uiFactories)
            .setAnimatedNavDecoratorFactory(CrossFadeNavDecoratorFactory())
            .build()
    }

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            val configSettings by lazy {
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = if (com.ninecraft.booket.BuildConfig.DEBUG) 0 else 60
                }
            }
            setConfigSettingsAsync(configSettings)
        }
    }

    @Provides
    fun provideFirebaseMessaging(): FirebaseMessaging = Firebase.messaging

    @Provides
    fun provideFirebaseInstallations(): FirebaseInstallations = Firebase.installations

    @Provides
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    val circuit: Circuit

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
