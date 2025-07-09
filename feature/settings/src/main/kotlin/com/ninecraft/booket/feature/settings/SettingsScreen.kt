package com.ninecraft.booket.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.appbar.ReedBackTopAppBar
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
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent
}

@CircuitInject(SettingsScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Settings(
    state: SettingsScreen.State,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        ReedBackTopAppBar(
            title = stringResource(R.string.settings_title),
            onNavigateBack = {
                // TODO: 뒤로가기 이벤트
            },
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))

        SettingItem(
            title = stringResource(R.string.settings_privacy_policy),
            onItemClick = {
                // TODO: 웹페이지로 이동
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
                // TODO: 웹페이지로 이동
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
                // TODO: 웹페이지로 이동
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
                    color = ReedTheme.colors.contentSecondary
                )
            }
        )
        ReedDivider(modifier = Modifier.padding(vertical = ReedTheme.spacing.spacing4))
        SettingItem(
            title = stringResource(R.string.settings_logout),
            onItemClick = {
                // TODO: 로그아웃
            },
        )
        SettingItem(
            title = stringResource(R.string.settings_withdraw),
            onItemClick = {
                // TODO: 회원탈퇴
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

@DevicePreview
@Composable
private fun SettingsScreenPreview() {
    ReedTheme {
        Settings(
            state = SettingsScreen.State(
                eventSink = {},
            ),
        )
    }
}
