package com.ninecraft.booket.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.feature.screens.LoginScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun LoginUi(
    state: LoginUiState,
    modifier: Modifier = Modifier,
) {
    HandleLoginSideEffects(
        state = state,
        eventSink = state.eventSink,
    )

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "로그인",
                    modifier = Modifier.align(Alignment.Center),
                )
                ReedButton(
                    onClick = {
                        state.eventSink(LoginUiEvent.OnKakaoLoginButtonClick)
                    },
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.KAKAO,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = ReedTheme.spacing.spacing5,
                            end = ReedTheme.spacing.spacing5,
                            bottom = ReedTheme.spacing.spacing8,
                        )
                        .align(Alignment.BottomCenter),
                    text = stringResource(id = R.string.kakao_login),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_kakao),
                            contentDescription = "Kakao Icon",
                            tint = Color.Unspecified,
                        )
                    },
                )

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ReedTheme.colors.contentBrand,
                    )
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun LoginPreview() {
    ReedTheme {
        LoginUi(
            state = LoginUiState(
                eventSink = {},
            ),
        )
    }
}
