package com.ninecraft.booket.di

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import com.ninecraft.booket.core.di.DataScope
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import kotlin.reflect.KClass

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
    val activityProviders: Map<KClass<out Activity>, Provider<Activity>>

    @Multibinds(allowEmpty = true)
    val serviceProviders: Map<KClass<out Service>, Provider<Service>>

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

    val circuit: Circuit

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
