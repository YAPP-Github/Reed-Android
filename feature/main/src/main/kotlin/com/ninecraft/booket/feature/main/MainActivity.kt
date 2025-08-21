package com.ninecraft.booket.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ninecraft.booket.core.common.constants.ErrorDialogSpec
import com.ninecraft.booket.core.common.event.ErrorEvent
import com.ninecraft.booket.core.common.event.ErrorEventHelper
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.feature.screens.SplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import dagger.hilt.android.AndroidEntryPoint
import tech.thdev.compose.exteions.system.ui.controller.rememberSystemUiController
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()

            DisposableEffect(systemUiController) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = true,
                    isNavigationBarContrastEnforced = false,
                )

                onDispose {}
            }

            ReedTheme {
                val backStack = rememberSaveableBackStack(root = SplashScreen)
                val navigator = rememberCircuitNavigator(backStack)

                val dialogSpec = remember { mutableStateOf<ErrorDialogSpec?>(null) }

                // 전역 에러 수신
                LaunchedEffect(Unit) {
                    ErrorEventHelper.errorEvent.collect { event ->
                        when (event) {
                            is ErrorEvent.ShowDialog -> {
                                dialogSpec.value = event.spec
                            }
                        }
                    }
                }

                dialogSpec.value?.let { spec ->
                    ReedDialog(
                        description = spec.message,
                        confirmButtonText = stringResource(spec.buttonLabelResId),

                        onConfirmRequest = {
                            spec.action()
                            dialogSpec.value = null
                        },
                        onDismissRequest = {
                            dialogSpec.value = null
                        },
                    )
                }

                CircuitCompositionLocals(circuit) {
                    NavigableCircuitContent(
                        navigator = navigator,
                        backStack = backStack,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
