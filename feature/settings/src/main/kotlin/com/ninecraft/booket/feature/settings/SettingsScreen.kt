package com.ninecraft.booket.feature.settings

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object SettingsScreen : Screen {
    data class State(
        val isLogoutSheetVisible: Boolean,
        val isWithdrawSheetVisible: Boolean,
        val isWithdrawConfirmed: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object OnBackClick : Event
        data class OnTermDetailClick(val title: String) : Event
        data object OnLogoutClick : Event
        data object OnWithdrawClick : Event
        data object OnBottomSheetDismissed : Event
        data object OnWithdrawConfirmationToggled : Event
        data object Logout : Event
        data object Withdraw : Event
    }
}

@CircuitInject(SettingsScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Settings(
    state: SettingsScreen.State,
    modifier: Modifier = Modifier,
) {

    if (state.isLogoutSheetVisible) {
        LogoutConfirmationBottomSheet(
            onDismissRequest = {
                state.eventSink(SettingsScreen.Event.OnBottomSheetDismissed)
            },
            onCancelButtonClick = {
                state.eventSink(SettingsScreen.Event.OnBottomSheetDismissed)
            },
            onLogoutButtonClick = {
                state.eventSink(SettingsScreen.Event.Logout)
            },
        )
    }

    if (state.isWithdrawSheetVisible) {
        WithdrawConfirmationBottomSheet(
            onDismissRequest = {
                state.eventSink(SettingsScreen.Event.OnBottomSheetDismissed)
            },
            isCheckBoxChecked = state.isWithdrawConfirmed,
            onCheckBoxCheckedChange = {
                state.eventSink(SettingsScreen.Event.OnWithdrawConfirmationToggled)
            },
            onCancelButtonClick = {
                state.eventSink(SettingsScreen.Event.OnBottomSheetDismissed)
            },
            onWithdrawButtonClick = {
                state.eventSink(SettingsScreen.Event.Withdraw)
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        ReedBackTopAppBar(
            title = stringResource(R.string.settings_title),
            onNavigateBack = {
                state.eventSink(SettingsScreen.Event.OnBackClick)
            },
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))

        SettingItem(
            title = stringResource(R.string.settings_privacy_policy),
            onItemClick = {
                state.eventSink(SettingsScreen.Event.OnTermDetailClick(""))
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
                state.eventSink(SettingsScreen.Event.OnTermDetailClick(""))
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
                state.eventSink(SettingsScreen.Event.OnTermDetailClick(""))
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
            title = stringResource(R.string.settings_app_verision),
            action = {
                Text(
                    text = "1.0.1", // Presenter에 context 주입해서 버전 정보 가져오는 로직을 구현 해야할지 고민
                    style = ReedTheme.typography.body1Medium,
                    color = ReedTheme.colors.contentSecondary,
                )
            },
        )
        ReedDivider(modifier = Modifier.padding(vertical = ReedTheme.spacing.spacing4))
        SettingItem(
            title = stringResource(R.string.settings_logout),
            onItemClick = {
                state.eventSink(SettingsScreen.Event.OnLogoutClick)
            },
        )
        SettingItem(
            title = stringResource(R.string.settings_withdraw),
            onItemClick = {
                state.eventSink(SettingsScreen.Event.OnWithdrawClick)
            },
        )
    }
}

@Composable
private fun SettingItem(
    title: String,
    onItemClick: () -> Unit = {},
    action: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableSingle {
                onItemClick()
            }
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
fun LogoutConfirmationBottomSheet(
    onDismissRequest: () -> Unit,
    onCancelButtonClick: () -> Unit,
    onLogoutButtonClick: () -> Unit,
) {
    ReedBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
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
                text = stringResource(R.string.setting_logout_title),
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
                        onCancelButtonClick()
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
    isCheckBoxChecked: Boolean,
    onCheckBoxCheckedChange: () -> Unit,
    onCancelButtonClick: () -> Unit,
    onWithdrawButtonClick: () -> Unit,
) {
    ReedBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
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
                        onCancelButtonClick()
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
            state = SettingsScreen.State(
                isLogoutSheetVisible = false,
                isWithdrawSheetVisible = false,
                isWithdrawConfirmed = false,
                eventSink = {},
            ),
        )
    }
}
