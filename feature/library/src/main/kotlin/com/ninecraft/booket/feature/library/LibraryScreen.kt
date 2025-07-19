package com.ninecraft.booket.feature.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.screens.LibraryScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(LibraryScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Library(
    state: LibraryUiState,
    modifier: Modifier = Modifier,
) {
    HandleLibrarySideEffects(state = state)

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

@Composable
internal fun LibraryContent(
    state: LibraryUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    state.eventSink(LibraryUiEvent.OnSettingsClick)
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = com.ninecraft.booket.core.designsystem.R.drawable.ic_settings),
                    contentDescription = "Settings Icon",
                    tint = Color.Unspecified,
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "내 서재")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = state.nickname)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = state.email)
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = ReedTheme.colors.contentBrand,
                )
            }
        }
    }
}

@DevicePreview
@Composable
private fun LibraryPreview() {
    ReedTheme {
        Library(
            state = LibraryUiState(
                nickname = "홍길동",
                email = "test@test.com",
                eventSink = {},
            ),
        )
    }
}
