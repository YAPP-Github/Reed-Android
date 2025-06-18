package com.ninecraft.booket.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ninecraft.booket.ui.theme.BooketAndroidTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val backStack = rememberSaveableBackStack(root = ListScreen)
            val navigator = rememberCircuitNavigator(backStack)
            val systemUiController = rememberExSystemUiController()

            CircuitCompositionLocals(circuit) {
                PokedexScaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MainBottomBar(
                            navigator = navigator,
                            backStack = backStack,
                        )
                    },
                ) { innerPadding ->
                    ContentWithOverlays {
                        NavigableCircuitContent(
                            navigator = navigator,
                            backStack = backStack,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        )
                    }
                }
            }
        }
    }
}
