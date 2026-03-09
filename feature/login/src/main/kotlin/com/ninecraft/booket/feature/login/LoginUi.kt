package com.ninecraft.booket.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.ReedTextButton
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.component.button.smallButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.model.LoginMethod
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedCloseTopAppBar
import com.ninecraft.booket.core.ui.component.ReedLoadingIndicator
import com.ninecraft.booket.feature.login.component.LoginTooltipBox
import com.ninecraft.booket.feature.screens.LoginScreen
import com.skydoves.compose.stability.runtime.TraceRecomposition
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@TraceRecomposition
@CircuitInject(LoginScreen::class, AppScope::class)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(innerPadding)
                .then(
                    if (state.showLoginTooltip) {
                        Modifier.noRippleClickable {
                            state.eventSink(LoginUiEvent.OnDismissLoginTooltip)
                        }
                    } else {
                        Modifier
                    },
                ),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (state.returnToScreen != null) {
                    ReedCloseTopAppBar(
                        onClose = {
                            state.eventSink(LoginUiEvent.OnCloseButtonClick)
                        },
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.img_reed_logo_big),
                    contentDescription = "Reed Logo",
                    modifier = Modifier.height(67.14.dp),
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
                Text(
                    text = stringResource(R.string.login_reed_slogan),
                    color = ReedTheme.colors.contentBrand,
                    style = ReedTheme.typography.headline2SemiBold,
                )
                Spacer(modifier = Modifier.weight(1f))

                // 카카오 로그인 버튼 + 툴팁
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ReedButton(
                        onClick = {
                            state.eventSink(LoginUiEvent.OnKakaoLoginButtonClick)
                        },
                        sizeStyle = largeButtonStyle,
                        colorStyle = ReedButtonColorStyle.KAKAO,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ReedTheme.spacing.spacing5),
                        text = stringResource(id = R.string.kakao_login),
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_kakao),
                                contentDescription = "Kakao Icon",
                                tint = Color.Unspecified,
                            )
                        },
                    )

                    if (state.showLoginTooltip && state.recentLoginMethod == LoginMethod.KAKAO) {
                        LoginTooltipBox(
                            messageResId = R.string.recent_login,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset {
                                    IntOffset(
                                        x = (-28).dp.roundToPx(),
                                        y = (-32).dp.roundToPx(),
                                    )
                                },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))

                // 구글 로그인 버튼 + 툴팁
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ReedButton(
                        onClick = {
                            state.eventSink(LoginUiEvent.OnGoogleLoginButtonClick)
                        },
                        sizeStyle = largeButtonStyle,
                        colorStyle = ReedButtonColorStyle.GOOGLE,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ReedTheme.spacing.spacing5),
                        text = stringResource(id = R.string.google_login),
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_google),
                                contentDescription = "Google Icon",
                                tint = Color.Unspecified,
                            )
                        },
                    )

                    if (state.showLoginTooltip && state.recentLoginMethod == LoginMethod.GOOGLE) {
                        LoginTooltipBox(
                            messageResId = R.string.recent_login,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset {
                                    IntOffset(
                                        x = (-28).dp.roundToPx(),
                                        y = (-32).dp.roundToPx(),
                                    )
                                },
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(
                        if (state.returnToScreen == null) ReedTheme.spacing.spacing3 else ReedTheme.spacing.spacing8,
                    ),
                )

                if (state.returnToScreen == null) {
                    ReedTextButton(
                        onClick = {
                            state.eventSink(LoginUiEvent.OnGuestLoginButtonClick)
                        },
                        text = stringResource(R.string.guest_login),
                        sizeStyle = smallButtonStyle,
                        colorStyle = ReedButtonColorStyle.TEXT,
                    )
                }
            }

            if (state.isLoading) {
                ReedLoadingIndicator()
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

@DevicePreview
@Composable
private fun LoginKakaoTooltipPreview() {
    ReedTheme {
        LoginUi(
            state = LoginUiState(
                showLoginTooltip = true,
                recentLoginMethod = LoginMethod.KAKAO,
                eventSink = {},
            ),
        )
    }
}

@DevicePreview
@Composable
private fun LoginGoogleTooltipPreview() {
    ReedTheme {
        LoginUi(
            state = LoginUiState(
                showLoginTooltip = true,
                recentLoginMethod = LoginMethod.GOOGLE,
                eventSink = {},
            ),
        )
    }
}
