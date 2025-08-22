package com.ninecraft.booket.core.common.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import com.ninecraft.booket.core.common.utils.MultipleEventsCutter
import com.ninecraft.booket.core.common.utils.get

// https://stackoverflow.com/questions/66703448/how-to-disable-ripple-effect-when-clicking-in-jetpack-compose
inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}

fun Modifier.clickableSingle(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    },
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        role = role,
        indication = ripple(),
        interactionSource = remember { MutableInteractionSource() },
    )
}

fun Modifier.captureToGraphicsLayer(graphicsLayer: GraphicsLayer) =
    this.drawWithContent {
        graphicsLayer.record { this@drawWithContent.drawContent() }
        drawLayer(graphicsLayer)
    }

/**
 * 부모 영역에서 동시 터치(두 손가락 이상)를 차단하는 Modifier
 */
fun Modifier.preventMultiTouch() = pointerInput(Unit) {
    // awaitEachGesture: 한 번의 제스쳐 세션을 추상화
    awaitEachGesture {
        val first = awaitFirstDown(requireUnconsumed = false)
        do {
            // 이벤트 전파 초기 단계(PointerEventPass.Initial)에서 하위 컴포저블로 이벤트가 내려가기 전에 가로채 소비한다
            val event = awaitPointerEvent(pass = PointerEventPass.Initial)
            event.changes.forEach { change ->
                if (change.id != first.id && change.pressed) change.consume()
            }
            // 루프 조건: 첫 포인터가 pressed 상태일 동안만 유지한다 (up이거나 cancel되면 pressed=false로 루프 종료)
        } while (event.changes.any { it.id == first.id && it.pressed })
    }
}
