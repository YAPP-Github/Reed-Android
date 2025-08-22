package com.ninecraft.booket.splash

import android.R.attr.description
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.feature.screens.SplashScreen
import com.ninecraft.booket.feature.splash.R
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import tech.thdev.compose.exteions.system.ui.controller.rememberSystemUiController

@CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
@Composable
fun SplashUi(
    state: SplashUiState,
    modifier: Modifier = Modifier,
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false,
            isNavigationBarContrastEnforced = false,
        )

        onDispose {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = true,
                isNavigationBarContrastEnforced = false,
            )
        }
    }

    HandleSplashSideEffects(
        state = state,
        eventSink = state.eventSink,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ReedTheme.colors.bgPrimary),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_reed_logo),
                contentDescription = "Reed Logo",
                modifier = Modifier.width(182.dp),
            )
            Spacer(Modifier.height(ReedTheme.spacing.spacing5))
            Text(
                text = stringResource(R.string.splash_title),
                color = ReedTheme.colors.contentInverse,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.heading2SemiBold,
            )
            Spacer(Modifier.height(ReedTheme.spacing.spacing8))
        }

        if (state.isForceUpdateDialogVisible) {
            ReedDialog(
                title = stringResource(R.string.splash_force_update_title),
                description = stringResource(R.string.splash_force_update_message),
                confirmButtonText = stringResource(R.string.splash_force_update_button_text),
                onConfirmRequest = {
                    state.eventSink(SplashUiEvent.OnUpdateButtonClick)
                },
            )
        }
    }
}

@DevicePreview
@Composable
private fun SplashPreview() {
    ReedTheme {
        SplashUi(
            state = SplashUiState(
                eventSink = {},
            ),
        )
    }
}
