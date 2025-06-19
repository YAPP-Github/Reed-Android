package com.ninecraft.booket.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.ui.theme.BooketTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent
}

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Home(
    state: HomeScreen.State,
    modifier: Modifier = Modifier,
) {
}

@Composable
internal fun HomeContent(
    state: HomeScreen.State,
    modifier: Modifier = Modifier,
) {
}

@DevicePreview
@Composable
private fun HomePreview() {
    BooketTheme {
        Home(
            state = HomeScreen.State(
                eventSink = {},
            ),
        )
    }
}
