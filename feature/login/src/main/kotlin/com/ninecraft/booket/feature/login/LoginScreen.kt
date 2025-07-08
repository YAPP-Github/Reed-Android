package com.ninecraft.booket.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object LoginScreen : Screen {
    data class State(
        val isLoading: Boolean = false,
        val sideEffect: SideEffect? = null,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface SideEffect {
        data object KakaoLogin : SideEffect
        data class ShowToast(val message: String) : SideEffect
    }

    sealed interface Event : CircuitUiEvent {
        data object InitSideEffect : Event
        data object OnKakaoLoginButtonClick : Event
        data class Login(val accessToken: String) : Event
        data class LoginFailure(val message: String) : Event
    }
}

@CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Login(
    state: LoginScreen.State,
    modifier: Modifier = Modifier,
) {
    HandleLoginSideEffects(
        state = state,
        eventSink = state.eventSink,
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                text = "로그인",
                modifier = Modifier.align(Alignment.Center),
            )
            ReedButton(
                onClick = {
                    state.eventSink(LoginScreen.Event.OnKakaoLoginButtonClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
                    .height(56.dp)
                    .align(Alignment.BottomCenter),
                colorStyle = ReedButtonColorStyle.KAKAO,
                sizeStyle = largeButtonStyle,
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
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@DevicePreview
@Composable
private fun LoginPreview() {
    ReedTheme {
        Login(
            state = LoginScreen.State(
                eventSink = {},
            ),
        )
    }
}
