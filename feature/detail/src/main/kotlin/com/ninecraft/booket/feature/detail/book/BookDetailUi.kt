package com.ninecraft.booket.feature.detail.book

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(BookDetailScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun BookDetailUi(
    state: BookDetailUiState,
    modifier: Modifier = Modifier,
) {
    HandleBookDetailSideEffects(
        state = state,
        eventSink = state.eventSink,
    )

    ReedFullScreen(modifier = modifier) {
        ReedBackTopAppBar(
            title = "",
            onBackClick = {
                state.eventSink(BookDetailUiEvent.OnBackClicked)
            },
        )
        BookDetailContent(state = state)
    }
}

@Composable
internal fun BookDetailContent(
    state: BookDetailUiState,
    modifier: Modifier = Modifier,
) {

}

@ComponentPreview
@Composable
private fun BookDetailPreview() {
    ReedTheme {
        BookDetailUi(
            state = BookDetailUiState(
                eventSink = {},
            ),
        )
    }
}
