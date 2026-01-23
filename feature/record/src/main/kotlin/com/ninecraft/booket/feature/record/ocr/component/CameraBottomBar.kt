package com.ninecraft.booket.feature.record.ocr.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.Neutral900
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun CameraBottomBar(
    onGalleryClick: () -> Unit,
    onCaptureClick: () -> Unit,
    buttonEnabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReedTheme.spacing.spacing6),
    ) {
        IconButton(
            onClick = onGalleryClick,
            modifier = Modifier
                .size(ReedTheme.spacing.spacing12)
                .align(Alignment.CenterStart),
            enabled = buttonEnabled,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Neutral900,
                contentColor = White,
            ),
            shape = RoundedCornerShape(ReedTheme.radius.sm),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(designR.drawable.ic_gallery),
                contentDescription = "Gallery Icon",
                modifier = Modifier
                    .size(ReedTheme.spacing.spacing6),
            )
        }
        Button(
            onClick = onCaptureClick,
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.Center),
            enabled = buttonEnabled,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = ReedTheme.colors.bgPrimary,
                contentColor = White,
            ),
            contentPadding = PaddingValues(ReedTheme.spacing.spacing0),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(designR.drawable.ic_maximize),
                contentDescription = "Scan Icon",
                modifier = Modifier.size(ReedTheme.spacing.spacing8),
            )
        }
    }
}

@ComponentPreview
@Composable
private fun CameraBottomBarPreview() {
    ReedTheme {
        CameraBottomBar(
            onGalleryClick = {},
            onCaptureClick = {},
        )
    }
}
