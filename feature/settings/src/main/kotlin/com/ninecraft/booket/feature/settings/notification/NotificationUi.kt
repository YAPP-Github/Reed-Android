package com.ninecraft.booket.feature.settings.notification

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
    HandleNotificationSideEffects(
        state = state,
        eventSink = state.eventSink,
    )

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val isGranted by produceState(
        initialValue = checkSystemNotificationEnabled(context),
        key1 = lifecycleOwner,
    ) {
        // 포그라운드 복귀 시 OS 권한 동기화
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                value = checkSystemNotificationEnabled(context)
                state.eventSink(NotificationUiEvent.OnNotificationPermissionResult(value))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        awaitDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { _ -> }

    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }

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
            if (!isGranted) {
                NotificationGuideItem(
                    onClick = {
                        settingsLauncher.launch(intent)
                    },
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            ToggleItem(
                title = stringResource(R.string.notification_toggle_title),
                description = stringResource(R.string.notification_toggle_description),
                isChecked = isGranted && state.isNotificationEnabled,
                onCheckedChange = { enabled ->
                    if (isGranted) {
                        state.eventSink(NotificationUiEvent.OnNotificationToggle(enabled))
                    } else {
                        settingsLauncher.launch(intent)
                    }
                },
            )
        }
    }
}

@Composable
internal fun NotificationGuideItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(
                vertical = ReedTheme.spacing.spacing2,
                horizontal = ReedTheme.spacing.spacing5,
            )
            .fillMaxWidth()
            .background(
                color = ReedTheme.colors.baseSecondary,
                shape = RoundedCornerShape(ReedTheme.radius.md),
            )
            .noRippleClickable { onClick() }
            .padding(
                vertical = ReedTheme.spacing.spacing6,
                horizontal = ReedTheme.spacing.spacing5,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
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

private fun checkSystemNotificationEnabled(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
