package com.ninecraft.booket.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ninecraft.booket.core.common.util.compareVersions
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.ReedDivider
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.core.ui.component.ReedLoadingIndicator
import com.ninecraft.booket.feature.screens.SettingsScreen
import com.ninecraft.booket.feature.settings.component.SettingItem
import com.ninecraft.booket.feature.settings.component.WithdrawConfirmationBottomSheet
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import com.ninecraft.booket.core.designsystem.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(SettingsScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun SettingsUi(
    state: SettingsUiState,
    modifier: Modifier = Modifier,
) {
    HandleSettingsSideEffects(
        state = state,
        eventSink = state.eventSink,
    )

    val withDrawSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val appVersion = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0)?.versionName
        }.getOrNull() ?: "Unknown"
    }

    val isUpdateAvailable = remember(appVersion, state.latestVersion) {
        compareVersions(state.latestVersion, appVersion) > 0
    }

    ReedScaffold(
        modifier = modifier
            .fillMaxSize()
            .background(White),
        containerColor = White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
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
                    state.eventSink(SettingsUiEvent.OnPolicyClick)
                },
                action = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = designR.drawable.ic_chevron_right),
                        contentDescription = "Right Chevron Icon",
                        tint = Color.Unspecified,
                    )
                },
            )
            SettingItem(
                title = stringResource(R.string.settings_terms_of_service),
                onItemClick = {
                    state.eventSink(SettingsUiEvent.OnTermClick)
                },
                action = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = designR.drawable.ic_chevron_right),
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
                        imageVector = ImageVector.vectorResource(id = designR.drawable.ic_chevron_right),
                        contentDescription = "Right Chevron Icon",
                        tint = Color.Unspecified,
                    )
                },
            )
            SettingItem(
                title = stringResource(R.string.settings_app_version),
                isClickable = isUpdateAvailable,
                onItemClick = {
                    state.eventSink(SettingsUiEvent.OnVersionClick)
                },
                action = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = appVersion,
                            style = ReedTheme.typography.body1Medium,
                            color = ReedTheme.colors.contentBrand,
                        )
                        if (isUpdateAvailable) {
                            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                            Icon(
                                imageVector = ImageVector.vectorResource(id = designR.drawable.ic_chevron_right),
                                contentDescription = "Right Chevron Icon",
                                tint = Color.Unspecified,
                            )
                        }
                    }
                },
                description = {
                    Text(
                        text = stringResource(R.string.latest_version, state.latestVersion),
                        color = ReedTheme.colors.contentTertiary,
                        style = ReedTheme.typography.label1Medium,
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

        if (state.isLoading) {
            ReedLoadingIndicator()
        }

        if (state.isLogoutDialogVisible) {
            ReedDialog(
                title = stringResource(R.string.settings_logout_title),
                confirmButtonText = stringResource(R.string.settings_logout),
                dismissButtonText = stringResource(R.string.settings_cancel),
                onConfirmRequest = {
                    state.eventSink(SettingsUiEvent.Logout)
                },
                onDismissRequest = {
                    state.eventSink(SettingsUiEvent.OnBottomSheetDismissed)
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

        if (state.isOptionalUpdateDialogVisible) {
            ReedDialog(
                onDismissRequest = {
                    state.eventSink(SettingsUiEvent.OnOptionalUpdateDialogDismiss)
                },
                title = stringResource(R.string.settings_optional_update_title),
                description = stringResource(R.string.settings_optional_update_message),
                confirmButtonText = stringResource(R.string.settings_optional_update_button_text),
                onConfirmRequest = {
                    state.eventSink(SettingsUiEvent.OnUpdateButtonClick)
                },
            )
        }
    }
}

@DevicePreview
@Composable
private fun SettingsScreenPreview() {
    ReedTheme {
        SettingsUi(
            state = SettingsUiState(
                eventSink = {},
            ),
        )
    }
}
