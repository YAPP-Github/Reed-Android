package com.ninecraft.booket.feature.record.ocr.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.Neutral800
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.record.R

@Composable
fun CameraFrame(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.Transparent)
            .border(
                width = ReedTheme.spacing.spacing5,
                color = Neutral800.copy(alpha = 0.6f),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.img_frame_marker),
            contentDescription = "Frame Marker",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                ),
            tint = ReedTheme.colors.bgPrimary,
        )
        Icon(
            painter = painterResource(R.drawable.img_frame_marker),
            contentDescription = "Frame Marker",
            modifier = Modifier
                .scale(scaleX = -1f, scaleY = 1f)
                .align(Alignment.TopEnd)
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                ),
            tint = ReedTheme.colors.bgPrimary,
        )
        Icon(
            painter = painterResource(R.drawable.img_frame_marker),
            contentDescription = "Frame Marker",
            modifier = Modifier
                .scale(scaleX = 1f, scaleY = -1f)
                .align(Alignment.BottomStart)
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                ),
            tint = ReedTheme.colors.bgPrimary,
        )
        Icon(
            painter = painterResource(R.drawable.img_frame_marker),
            contentDescription = "Frame Marker",
            modifier = Modifier
                .scale(scaleX = -1f, scaleY = -1f)
                .align(Alignment.BottomEnd)
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                ),
            tint = ReedTheme.colors.bgPrimary,
        )
    }
}

@ComponentPreview
@Composable
private fun CameraFramePreview() {
    ReedTheme {
        CameraFrame()
    }
}
