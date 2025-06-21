package com.ninecraft.booket.feature.login

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
data object LoginScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent
}

@CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Login(
    state: LoginScreen.State,
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
    state: LoginScreen.State,
    modifier: Modifier = Modifier,
) {
    Text(text = "로그인")
}

@DevicePreview
@Composable
private fun LibraryPreview() {
    BooketTheme {
        Login(
            state = LoginScreen.State(
                eventSink = {},
            ),
        )
    }
}
