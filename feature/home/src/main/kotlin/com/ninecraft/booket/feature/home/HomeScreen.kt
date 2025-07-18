package com.ninecraft.booket.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.screens.HomeScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Home(
    state: HomeUiState,
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
    state: HomeUiState,
    modifier: Modifier = Modifier,
) {
    Text(text = "홈")
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = {
            state.eventSink(HomeUiEvent.OnButtonClick)
        },
    ) {
        Text(text = "Navigate To Search")
    }
}

@DevicePreview
@Composable
private fun HomePreview() {
    ReedTheme {
        Home(
            state = HomeUiState(
                eventSink = {},
            ),
        )
    }
}
