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
import com.ninecraft.booket.screens.HomeScreen
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
