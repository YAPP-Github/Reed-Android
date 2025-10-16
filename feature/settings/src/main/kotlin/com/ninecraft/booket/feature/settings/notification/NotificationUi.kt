package com.ninecraft.booket.feature.settings.notification

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.feature.screens.NotificationScreen
import com.ninecraft.booket.feature.settings.R
import com.ninecraft.booket.feature.settings.component.ToggleItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import com.ninecraft.booket.core.designsystem.R as designR

@CircuitInject(NotificationScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun NotificationUi(
    state: NotificationUiState,
    modifier: Modifier = Modifier,
) {
    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ReedBackTopAppBar(
                modifier = modifier.fillMaxWidth(),
                title = stringResource(R.string.settings_notification),
                onBackClick = {
                    state.eventSink(NotificationUiEvent.OnBackClick)
                },
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            NotificationGuideItem()
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            ToggleItem(
                title = stringResource(R.string.notification_toggle_title),
                description = stringResource(R.string.notification_toggle_description),
                isChecked = state.isNotificationEnabled,
                onCheckedChange = { enabled ->
                    state.eventSink(NotificationUiEvent.OnNotificationToggle(enabled))
                },
            )
        }
    }
}

@Composable
internal fun NotificationGuideItem() {
    val context = LocalContext.current
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { _ -> }

    Row(
        modifier = Modifier
            .padding(horizontal = ReedTheme.spacing.spacing5)
            .fillMaxWidth()
            .background(
                color = ReedTheme.colors.baseSecondary,
                shape = RoundedCornerShape(ReedTheme.radius.md),
            )
            .noRippleClickable {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                settingsLauncher.launch(intent)
            }
            .padding(
                vertical = ReedTheme.spacing.spacing6,
                horizontal = ReedTheme.spacing.spacing5,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = stringResource(R.string.notification_guide_title),
                color = ReedTheme.colors.contentBrand,
                style = ReedTheme.typography.body1SemiBold,
            )
            Text(
                text = stringResource(R.string.notification_guide_description),
                color = ReedTheme.colors.contentTertiary,
                style = ReedTheme.typography.label2Regular,
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(designR.drawable.ic_chevron_right),
            contentDescription = "Chevron Right Icon",
            tint = ReedTheme.colors.contentBrand,
        )
    }
}

@DevicePreview
@Composable
private fun NotificationUiPreview() {
    ReedTheme {
        NotificationUi(
            state = NotificationUiState(
                eventSink = {},
            ),
        )
    }
}
