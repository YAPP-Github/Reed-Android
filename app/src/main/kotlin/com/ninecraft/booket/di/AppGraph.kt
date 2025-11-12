package com.ninecraft.booket.di

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.ninecraft.booket.core.di.DataScope
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.LocalCircuit
import com.slack.circuit.runtime.Navigator
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
            .setOnUnavailableContent { screen, modifier ->
                val circuit = LocalCircuit.current
                BasicText(
                    text = """
                      Route not available: ${screen.javaClass.name}.
                      Presenter: ${circuit?.presenter(screen, Navigator.NoOp)?.javaClass}
                      UI: ${circuit?.ui(screen)?.javaClass}
                      All presenterFactories: ${circuit?.newBuilder()?.presenterFactories}
                      All uiFactories: ${circuit?.newBuilder()?.uiFactories}
                      """
                        .trimIndent(),
                    modifier = modifier.background(Color.Red),
                    style = TextStyle(color = Color.Yellow),
                )
            }
            .build()
    }

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
