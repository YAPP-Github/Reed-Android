package com.ninecraft.booket.di

import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.LocalCircuit
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface CircuitGraph {

    @Multibinds(allowEmpty = true)
    fun presenterFactories(): Set<Presenter.Factory>

    @Multibinds(allowEmpty = true)
    fun uiFactories(): Set<Ui.Factory>

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
}
