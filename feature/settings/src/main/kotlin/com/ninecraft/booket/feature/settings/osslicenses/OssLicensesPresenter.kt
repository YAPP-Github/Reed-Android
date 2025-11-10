package com.ninecraft.booket.feature.settings.osslicenses

import androidx.compose.runtime.Composable
import com.ninecraft.booket.feature.screens.OssLicensesScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Inject

@Inject
@CircuitInject(OssLicensesScreen::class, AppScope::class)
class OssLicensesPresenter(
    @Assisted val navigator: Navigator,
) : Presenter<OssLicensesUiState> {
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
