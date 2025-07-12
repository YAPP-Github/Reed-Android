package com.ninecraft.booket.feature.settings.osslicenses

import androidx.compose.runtime.Composable
import com.ninecraft.booket.screens.OssLicensesScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class OssLicensesPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
) : Presenter<OssLicensesUiState> {
    @Composable
    override fun present(): OssLicensesUiState {
        fun handleEvent(event: OssLicensesUiEvent) {
            when (event) {
                is OssLicensesUiEvent.OnBackClicked -> {
                    navigator.pop()
                }
            }
        }
        return OssLicensesUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(OssLicensesScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OssLicensesPresenter
    }
}
