package com.ninecraft.booket.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.appbar.ReedBackTopAppBar
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(BookDetailScreen::class, ActivityRetainedComponent::class)
@Composable
fun BookDetail(
    state: BookDetailUiState,
    modifier: Modifier = Modifier,
) {
    ReedFullScreen(modifier = modifier) {
        ReedBackTopAppBar(
            title = "도서 상세 정보",
            onBackClick = {
                state.eventSink(BookDetailUiEvent.OnBackClicked)
            },
        )
    }
}

@ComponentPreview
@Composable
private fun BookDetailPreview() {
    ReedTheme {
        BookDetail(
            state = BookDetailUiState(
                eventSink = {},
            ),
        )
    }
}
