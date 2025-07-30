package com.ninecraft.booket.feature.main.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ninecraft.booket.feature.screens.SplashScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
@Composable
fun SplashUi(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize())
}
