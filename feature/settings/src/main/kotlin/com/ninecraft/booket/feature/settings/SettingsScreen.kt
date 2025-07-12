package com.ninecraft.booket.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.appbar.ReedBackTopAppBar
import com.ninecraft.booket.core.designsystem.component.divider.ReedDivider
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.settings.component.LogoutConfirmationBottomSheet
import com.ninecraft.booket.feature.settings.component.WithdrawConfirmationBottomSheet
import com.ninecraft.booket.screens.SettingsScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(SettingsScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Settings(
    state: SettingsUiState,
    modifier: Modifier = Modifier,
) {
    val logoutSheetState = rememberModalBottomSheetState()
    val withDrawSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val appVersion = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0)?.versionName
        }.getOrNull() ?: "Unknown"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        ReedBackTopAppBar(
            title = stringResource(R.string.settings_title),
            onBackClick = {
                state.eventSink(SettingsUiEvent.OnBackClick)
            },
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))

        SettingItem(
            title = stringResource(R.string.settings_privacy_policy),
            onItemClick = {
                state.eventSink(SettingsUiEvent.OnTermDetailClick(""))
            },
            action = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = com.ninecraft.booket.core.designsystem.R.drawable.ic_chevron_right),
                    contentDescription = "Right Chevron Icon",
                    tint = Color.Unspecified,
                )
            },
        )
        SettingItem(
            title = stringResource(R.string.settings_terms_of_service),
            onItemClick = {
                state.eventSink(SettingsUiEvent.OnTermDetailClick(""))
            },
            action = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = com.ninecraft.booket.core.designsystem.R.drawable.ic_chevron_right),
                    contentDescription = "Right Chevron Icon",
                    tint = Color.Unspecified,
                )
            },
        )
        SettingItem(
            title = stringResource(R.string.settings_open_source_license),
            onItemClick = {
                state.eventSink(SettingsUiEvent.OnOssLicensesClick)
            },
            action = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = com.ninecraft.booket.core.designsystem.R.drawable.ic_chevron_right),
                    contentDescription = "Right Chevron Icon",
                    tint = Color.Unspecified,
                )
            },
        )
        SettingItem(
            title = stringResource(R.string.settings_app_version),
            isClickable = false,
            action = {
                Text(
                    text = appVersion,
                    style = ReedTheme.typography.body1Medium,
                    color = ReedTheme.colors.contentSecondary,
                )
            },
        )
        ReedDivider(modifier = Modifier.padding(vertical = ReedTheme.spacing.spacing4))
        SettingItem(
            title = stringResource(R.string.settings_logout),
            onItemClick = {
                state.eventSink(SettingsUiEvent.OnLogoutClick)
            },
        )
        SettingItem(
            title = stringResource(R.string.settings_withdraw),
            onItemClick = {
                state.eventSink(SettingsUiEvent.OnWithdrawClick)
            },
        )
    }

    if (state.isLogoutBottomSheetVisible) {
        LogoutConfirmationBottomSheet(
            onDismissRequest = {
                state.eventSink(SettingsUiEvent.OnBottomSheetDismissed)
            },
            sheetState = logoutSheetState,
            onCancelButtonClick = {
                coroutineScope.launch {
                    logoutSheetState.hide()
                    state.eventSink(SettingsUiEvent.OnBottomSheetDismissed)
                }
            },
            onLogoutButtonClick = {
                state.eventSink(SettingsUiEvent.Logout)
            },
        )
    }

    if (state.isWithdrawBottomSheetVisible) {
        WithdrawConfirmationBottomSheet(
            onDismissRequest = {
                state.eventSink(SettingsUiEvent.OnBottomSheetDismissed)
            },
            sheetState = withDrawSheetState,
            isCheckBoxChecked = state.isWithdrawConfirmed,
            onCheckBoxCheckedChange = {
                state.eventSink(SettingsUiEvent.OnWithdrawConfirmationToggled)
            },
            onCancelButtonClick = {
                coroutineScope.launch {
                    withDrawSheetState.hide()
                    state.eventSink(SettingsUiEvent.OnBottomSheetDismissed)
                }
            },
            onWithdrawButtonClick = {
                state.eventSink(SettingsUiEvent.Withdraw)
            },
        )
    }
}

@Composable
private fun SettingItem(
    title: String,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true,
    onItemClick: () -> Unit = {},
    action: @Composable () -> Unit = {},
) {
    val combinedModifier = if (isClickable) {
        modifier
            .fillMaxWidth()
            .clickableSingle { onItemClick() }
    } else {
        modifier.fillMaxWidth()
    }

    Row(
        modifier = combinedModifier
            .padding(
                horizontal = ReedTheme.spacing.spacing5,
                vertical = ReedTheme.spacing.spacing4,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = ReedTheme.typography.body1Medium,
            color = ReedTheme.colors.contentPrimary,
        )
        action()
    }
}

@DevicePreview
@Composable
private fun SettingsScreenPreview() {
    ReedTheme {
        Settings(
            state = SettingsUiState(
                isLogoutBottomSheetVisible = false,
                isWithdrawBottomSheetVisible = false,
                isWithdrawConfirmed = false,
                eventSink = {},
            ),
        )
    }
}
