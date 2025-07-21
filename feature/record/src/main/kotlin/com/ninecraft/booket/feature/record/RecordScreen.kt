package com.ninecraft.booket.feature.record

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(RecordScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Record(
    state: RecordUiState,
    modifier: Modifier = Modifier,
) {
    BackHandler {}

    ReedFullScreen(modifier = modifier) {}
}

@DevicePreview
@Composable
private fun RecordPreview() {
    ReedTheme {
        Record(
            state = RecordUiState(
                eventSink = {},
            ),
        )
    }
}
