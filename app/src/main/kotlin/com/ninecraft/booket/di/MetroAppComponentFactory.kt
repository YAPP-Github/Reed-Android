package com.ninecraft.booket.di

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Intent
import androidx.annotation.Keep
import androidx.core.app.AppComponentFactory
import com.ninecraft.booket.BooketApplication
import dev.zacsweers.metro.Provider
import kotlin.reflect.KClass

/**
 * AppComponentFactory that uses Metro for constructor injection of Activities and Services.
 *
 * Requires minSdk 28+. For lower versions, use manual graph access.
 */
@Keep
class MetroAppComponentFactory : AppComponentFactory() {

    private inline fun <reified T : Any> getInstance(
        cl: ClassLoader,
        className: String,
        providers: Map<KClass<out T>, Provider<T>>,
    ): T? {
        val clazz = Class.forName(className, false, cl).asSubclass(T::class.java)
        val modelProvider = providers[clazz.kotlin] ?: return null
        return modelProvider()
    }

    override fun instantiateActivityCompat(
        cl: ClassLoader,
        className: String,
        intent: Intent?
    ): Activity {
        return getInstance(cl, className, activityProviders)
            ?: super.instantiateActivityCompat(cl, className, intent)
    }

    override fun instantiateServiceCompat(
        cl: ClassLoader,
        className: String,
        intent: Intent?
    ): Service {
        return getInstance(cl, className, serviceProviders)
            ?: super.instantiateServiceCompat(cl, className, intent)
    }

    override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application {
        val app = super.instantiateApplicationCompat(cl, className)
        val appGraph = (app as BooketApplication).appGraph
        activityProviders = appGraph.activityProviders
        serviceProviders = appGraph.serviceProviders
        return app
    }

    companion object {
        private lateinit var activityProviders: Map<KClass<out Activity>, Provider<Activity>>
        private lateinit var serviceProviders: Map<KClass<out Service>, Provider<Service>>
    }
}
