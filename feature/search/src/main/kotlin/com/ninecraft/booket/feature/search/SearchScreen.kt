package com.ninecraft.booket.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.screens.SearchScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(SearchScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Search(
    state: SearchUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SearchContent(
            state = state,
            modifier = modifier,
        )
    }
}

@Suppress("unused")
@Composable
internal fun SearchContent(
    state: SearchUiState,
    modifier: Modifier = Modifier,
) {
    Text(text = "도서 검색")
}

@DevicePreview
@Composable
private fun SearchPreview() {
    ReedTheme {
        Search(
            state = SearchUiState(
                eventSink = {},
            ),
        )
    }
}
