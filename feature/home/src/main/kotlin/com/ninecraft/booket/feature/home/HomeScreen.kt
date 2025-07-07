package com.ninecraft.booket.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
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
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        HomeContent(
            state = state,
            modifier = modifier,
        )
    }
}

@Suppress("unused")
@Composable
internal fun HomeContent(
    state: HomeScreen.State,
    modifier: Modifier = Modifier,
) {
    Text(text = "홈")
}

@DevicePreview
@Composable
private fun HomePreview() {
    ReedTheme {
        Home(
            state = HomeScreen.State(
                eventSink = {},
            ),
        )
    }
}
