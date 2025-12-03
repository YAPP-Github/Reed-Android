package com.ninecraft.booket.feature.settings.component

import android.R.attr.x
import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.skydoves.compose.stability.runtime.TraceRecomposition

@TraceRecomposition
@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
internal fun ReedSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val transition = updateTransition(checked, label = "switchTransition")

    val trackColor by transition.animateColor(label = "trackColor") {
        if (it) ReedTheme.colors.contentBrand else Color(0xFFE9E9EB)
    }

    val thumbOffset by transition.animateDp(label = "thumbOffset") {
        if (it) 22.dp else 2.dp
    }

    Box(
        modifier = modifier
            .width(51.dp)
            .height(31.dp)
            .clip(RoundedCornerShape(ReedTheme.radius.full))
            .background(trackColor)
            .noRippleClickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = thumbOffset.roundToPx(),
                        y = 0,
                    )
                }
                .size(27.dp)
                .shadow(elevation = 1.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(ReedTheme.colors.contentInverse),
        )
    }
}

@DevicePreview
@Composable
private fun ReedSwitchPreview() {
    var isChecked by remember { mutableStateOf(true) }

    ReedTheme {
        ReedSwitch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
        )
    }
}
