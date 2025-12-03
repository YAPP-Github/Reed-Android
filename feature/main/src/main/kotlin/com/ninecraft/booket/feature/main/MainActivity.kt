package com.ninecraft.booket.feature.main

import android.app.Activity
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ninecraft.booket.core.common.event.DialogSpec
import com.ninecraft.booket.core.common.event.EventHelper
import com.ninecraft.booket.core.common.event.ReedEvent
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.core.di.ActivityKey
import com.ninecraft.booket.feature.screens.SplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import tech.thdev.compose.exteions.system.ui.controller.rememberSystemUiController

@ContributesIntoMap(AppScope::class, binding = binding<Activity>())
@ActivityKey(MainActivity::class)
@Inject
class MainActivity(
    private val circuit: Circuit,
) : ComponentActivity() {

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

                val dialogSpec = remember { mutableStateOf<DialogSpec?>(null) }

                // 전역 이벤트 수신
                LaunchedEffect(Unit) {
                    EventHelper.eventFlow.collect { event ->
                        when (event) {
                            is ReedEvent.ShowDialog -> {
                                dialogSpec.value = event.spec
                            }
                        }
                    }
                }

                dialogSpec.value?.let { spec ->
                    ReedDialog(
                        title = spec.title,
                        description = spec.message,
                        confirmButtonText = spec.confirmLabel,
                        dismissButtonText = spec.dismissLabel,
                        onConfirmRequest = {
                            spec.onConfirm()
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
