package com.ninecraft.booket.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import kotlinx.coroutines.delay

@Composable
fun ReedLoadingIndicator(
    modifier: Modifier = Modifier,
    delayMillis: Long = 500L,
) {
    val showProgressBar by produceState(initialValue = false, key1 = delayMillis) {
        delay(delayMillis)
        value = true
    }

    if (showProgressBar) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .noRippleClickable {},
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = ReedTheme.colors.contentBrand)
        }
    }
}

@ComponentPreview
@Composable
private fun ReedLoadingIndicatorPreview() {
    ReedTheme {
        ReedLoadingIndicator()
    }
}
