package com.ninecraft.booket.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Home
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.ui.theme.BooketTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object SearchScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent
}

@CircuitInject(SearchScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Search(
    state: SearchScreen.State,
    modifier: Modifier = Modifier,
) {
}

@Composable
internal fun SearchContent(
    state: SearchScreen.State,
    modifier: Modifier = Modifier,
) {
}

@DevicePreview
@Composable
private fun SearchPreview() {
    BooketTheme {
        Search(
            state = SearchScreen.State(
                eventSink = {},
            ),
        )
    }
}
