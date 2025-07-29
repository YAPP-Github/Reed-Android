package com.ninecraft.booket.feature.search.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.screens.LibrarySearchScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(LibrarySearchScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun LibrarySearchUi(
    state: LibrarySearchUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LibrarySearchContent(
            state = state,
            modifier = modifier,
        )
    }
}

@Composable
internal fun LibrarySearchContent(
    state: LibrarySearchUiState,
    modifier: Modifier = Modifier,
) {
    Text(text = "내서재 검색")
}

@DevicePreview
@Composable
private fun LibrarySearchPreview() {
    ReedTheme {
        LibrarySearchUi(
            state = LibrarySearchUiState(
                eventSink = {},
            ),
        )
    }
}
