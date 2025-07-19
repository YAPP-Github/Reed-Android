package com.ninecraft.booket.feature.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.library.component.FilterChipGroup
import com.ninecraft.booket.feature.library.component.LibraryHeader
import com.ninecraft.booket.feature.screens.LibraryScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf

@CircuitInject(LibraryScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Library(
    state: LibraryUiState,
    modifier: Modifier = Modifier,
) {
    HandleLibrarySideEffects(
        state = state,
        eventSink = state.eventSink,
    )

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
        LibraryHeader(
            onSearchClick = {
                // TODO: 내서재 검색 화면으로 이동
            },
            onSettingClick = {
                state.eventSink(LibraryUiEvent.OnSettingsClick)
            },
        )
        FilterChipGroup(
            filterList = state.filterElements,
            onChipClick = { status ->
                state.eventSink(LibraryUiEvent.OnFilterClick(status))
            },
        )
        EmptyBookListScreen()
    }
}

@Composable
private fun EmptyBookListScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.library_empty_book_title),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.headline1SemiBold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            Text(
                text = stringResource(R.string.library_empty_book_description),
                color = ReedTheme.colors.contentSecondary,
                style = ReedTheme.typography.body1Medium,
            )
        }
    }
}

@DevicePreview
@Composable
private fun LibraryPreview() {
    ReedTheme {
        val filterList = persistentListOf(
            FilterChipState(
                title = BookStatus.TOTAL,
                count = 10,
                isSelected = true,
            ),
            FilterChipState(
                title = BookStatus.BEFORE_READING,
                count = 15,
                isSelected = false,
            ),
            FilterChipState(
                title = BookStatus.READING,
                count = 2,
                isSelected = false,
            ),
            FilterChipState(
                title = BookStatus.COMPLETED,
                count = 5,
                isSelected = false,
            ),
        )
        Library(
            state = LibraryUiState(
                filterElements = filterList,
                eventSink = {},
            ),
        )
    }
}
