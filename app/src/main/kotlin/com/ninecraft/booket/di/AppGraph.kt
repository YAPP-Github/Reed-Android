package com.ninecraft.booket.di

import android.app.Activity
import android.app.Service
import android.content.Context
import com.ninecraft.booket.core.di.ApplicationContext
import com.ninecraft.booket.core.di.DataScope
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

    @Multibinds(allowEmpty = true)
    val activityProviders: Map<KClass<out Activity>, Provider<Activity>>

    @Multibinds(allowEmpty = true)
    val serviceProviders: Map<KClass<out Service>, Provider<Service>>

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@ApplicationContext @Provides context: Context): AppGraph
    }
}
