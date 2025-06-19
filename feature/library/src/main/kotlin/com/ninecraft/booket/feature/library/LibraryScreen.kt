package com.ninecraft.booket.feature.library

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
data object LibraryScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent
}

@CircuitInject(LibraryScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Library(
    state: LibraryScreen.State,
    modifier: Modifier = Modifier,
) {
}

@Composable
internal fun LibraryContent(
    state: LibraryScreen.State,
    modifier: Modifier = Modifier,
) {
}

@DevicePreview
@Composable
private fun LibraryPreview() {
    BooketTheme {
        Library(
            state = LibraryScreen.State(
                eventSink = {},
            ),
        )
    }
}
