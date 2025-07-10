package com.ninecraft.booket.feature.settings

import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.appbar.ReedBackTopAppBar
import com.ninecraft.booket.core.designsystem.component.bottomsheet.ReedBottomSheet
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.component.checkbox.SquareCheckBox
import com.ninecraft.booket.core.designsystem.component.divider.ReedDivider
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.screens.SettingsScreen
import com.orhanobut.logger.Logger
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
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "Unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.e(e, "Failed to get app version")
            "Unknown"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        ReedBackTopAppBar(
            title = stringResource(R.string.settings_title),
            onNavigateBack = {
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

    if (state.isLogoutSheetVisible) {
        LogoutConfirmationBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    logoutSheetState.hide()
                    state.eventSink(SettingsUiEvent.OnBottomSheetDismissed)
                }
            },
            sheetState = logoutSheetState,
            onLogoutButtonClick = {
                state.eventSink(SettingsUiEvent.Logout)
            },
        )
    }

    if (state.isWithdrawSheetVisible) {
        WithdrawConfirmationBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    withDrawSheetState.hide()
                    state.eventSink(SettingsUiEvent.OnBottomSheetDismissed)
                }
            },
            sheetState = withDrawSheetState,
            isCheckBoxChecked = state.isWithdrawConfirmed,
            onCheckBoxCheckedChange = {
                state.eventSink(SettingsUiEvent.OnWithdrawConfirmationToggled)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LogoutConfirmationBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onLogoutButtonClick: () -> Unit,
) {
    ReedBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                    end = ReedTheme.spacing.spacing5,
                ),
        ) {
            Text(
                text = stringResource(R.string.settings_logout_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ReedTheme.spacing.spacing3),
                color = ReedTheme.colors.contentPrimary,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.heading2SemiBold,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ReedButton(
                    onClick = {
                        onDismissRequest()
                    },
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.SECONDARY,
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.settings_cancel),
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                ReedButton(
                    onClick = {
                        onLogoutButtonClick()
                    },
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.settings_logout),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WithdrawConfirmationBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    isCheckBoxChecked: Boolean,
    onCheckBoxCheckedChange: () -> Unit,
    onWithdrawButtonClick: () -> Unit,
) {
    ReedBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                    end = ReedTheme.spacing.spacing5,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.settings_withdraw_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = ReedTheme.spacing.spacing3),
                color = ReedTheme.colors.contentPrimary,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.heading2SemiBold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            Text(
                text = stringResource(R.string.settings_withdraw_detail),
                modifier = Modifier.fillMaxWidth(),
                color = ReedTheme.colors.contentSecondary,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
            Row {
                SquareCheckBox(
                    checked = isCheckBoxChecked,
                    onCheckedChange = {
                        onCheckBoxCheckedChange()
                    },
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                Text(
                    text = stringResource(R.string.settings_withdraw_agreement),
                    color = ReedTheme.colors.contentPrimary,
                    textAlign = TextAlign.Center,
                    style = ReedTheme.typography.body1Medium,
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ReedButton(
                    onClick = {
                        onDismissRequest()
                    },
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.SECONDARY,
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.settings_cancel),
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                ReedButton(
                    onClick = {
                        onWithdrawButtonClick()
                    },
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    modifier = Modifier.weight(1f),
                    enabled = isCheckBoxChecked,
                    text = stringResource(R.string.settings_withdraw_action),
                )
            }
        }
    }
}

@DevicePreview
@Composable
private fun SettingsScreenPreview() {
    ReedTheme {
        Settings(
            state = SettingsUiState(
                isLogoutSheetVisible = false,
                isWithdrawSheetVisible = false,
                isWithdrawConfirmed = false,
                eventSink = {},
            ),
        )
    }
}
