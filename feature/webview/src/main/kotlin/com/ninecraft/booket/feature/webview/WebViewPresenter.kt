package com.ninecraft.booket.feature.webview

import androidx.compose.runtime.Composable
import com.ninecraft.booket.feature.screens.WebViewScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Inject

@Inject
@CircuitInject(WebViewScreen::class, AppScope::class)
class WebViewPresenter(
    @Assisted private val screen: WebViewScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<WebViewUiState> {

    @Composable
    override fun present(): WebViewUiState {
        fun handleEvent(event: WebViewUiEvent) {
            when (event) {
                is WebViewUiEvent.OnBackButtonClick -> {
                    navigator.pop()
                }
            }
        }

        return WebViewUiState(
            url = screen.url,
            title = screen.title,
            eventSink = ::handleEvent,
        )
    }
}
