package com.ninecraft.booket.feature.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
internal fun PagerIndicator(
    pageCount: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(32.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) ReedTheme.colors.bgPrimary else ReedTheme.colors.bgSecondaryPressed

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp),
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun PagerIndicatorPreview() {
    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = { pageCount })

    ReedTheme {
        PagerIndicator(
            pageCount = pageCount,
            pagerState = pagerState,
        )
    }
}
