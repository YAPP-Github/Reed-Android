package com.ninecraft.booket.feature.settings.osslicenses

import androidx.compose.runtime.Composable
import com.ninecraft.booket.feature.screens.OssLicensesScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
class OssLicensesPresenter(
    @Assisted val navigator: Navigator,
) : Presenter<OssLicensesUiState> {

    @CircuitInject(OssLicensesScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OssLicensesPresenter
    }

    @Composable
    override fun present(): OssLicensesUiState {
        fun handleEvent(event: OssLicensesUiEvent) {
            when (event) {
                is OssLicensesUiEvent.OnBackClick -> {
                    navigator.pop()
                }
            }
        }
        return OssLicensesUiState(
            eventSink = ::handleEvent,
        )
    }
}
