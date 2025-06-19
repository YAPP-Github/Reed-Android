package com.ninecraft.booket.feature.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.BooketTheme
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
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LibraryContent(
            state = state,
            modifier = modifier,
        )
    }
}

@Suppress("unused")
@Composable
internal fun LibraryContent(
    state: LibraryScreen.State,
    modifier: Modifier = Modifier,
) {
    Text(text = "내 서재")
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
