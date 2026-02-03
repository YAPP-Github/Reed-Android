package com.ninecraft.booket.core.common.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.ripple
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import com.ninecraft.booket.core.common.utils.MultipleEventsCutter
import com.ninecraft.booket.core.common.utils.get

// https://stackoverflow.com/questions/66703448/how-to-disable-ripple-effect-when-clicking-in-jetpack-compose
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = null,
        indication = null,
        onClick = onClick,
    )

fun Modifier.clickableSingle(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = this then ClickableSingleElement(enabled, onClickLabel, role, onClick)

private data class ClickableSingleElement(
    private val enabled: Boolean,
    private val onClickLabel: String?,
    private val role: Role?,
    private val onClick: () -> Unit,
) : ModifierNodeElement<ClickableSingleNode>() {
    override fun create() = ClickableSingleNode(enabled, onClickLabel, role, onClick)
    override fun update(node: ClickableSingleNode) {
        node.update(enabled, onClickLabel, role, onClick)
    }
}

private class ClickableSingleNode(
    private var enabled: Boolean,
    private var onClickLabel: String?,
    private var role: Role?,
    private var onClick: () -> Unit,
) : DelegatingNode(), SemanticsModifierNode {
    private val multipleEventsCutter = MultipleEventsCutter.get()
    private val interactionSource = MutableInteractionSource()

    @Suppress("unused")
    private val indicationNode = delegate(
        ripple().create(interactionSource),
    )

    @Suppress("unused")
    private val pointerInputNode = delegate(
        SuspendingPointerInputModifierNode {
            if (!enabled) return@SuspendingPointerInputModifierNode
            detectTapGestures(
                onPress = { offset ->
                    val press = PressInteraction.Press(offset)
                    interactionSource.emit(press)
                    val released = tryAwaitRelease()
                    if (released) {
                        interactionSource.emit(PressInteraction.Release(press))
                    } else {
                        interactionSource.emit(PressInteraction.Cancel(press))
                    }
                },
                onTap = {
                    multipleEventsCutter.processEvent { onClick() }
                },
            )
        },
    )

    override fun SemanticsPropertyReceiver.applySemantics() {
        onClick(label = onClickLabel) {
            if (!enabled) return@onClick false
            multipleEventsCutter.processEvent { this@ClickableSingleNode.onClick() }
            true
        }
        this@ClickableSingleNode.role?.let { this.role = it }
        if (!enabled) {
            disabled()
        }
    }

    fun update(enabled: Boolean, onClickLabel: String?, role: Role?, onClick: () -> Unit) {
        this.enabled = enabled
        this.onClickLabel = onClickLabel
        this.role = role
        this.onClick = onClick
    }
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

        while (true) {
            // 이벤트 전파 초기 단계(PointerEventPass.Initial)에서 하위 컴포저블로 이벤트가 내려가기 전에 가로채 소비한다
            val event = awaitPointerEvent(pass = PointerEventPass.Initial)
            event.changes.forEach { change ->
                if (change.id != first.id && change.pressed) {
                    change.consume()
                }
            }
            // 첫 포인터가 pressed 상태일 동안만 유지한다 (up이거나 cancel되면 pressed=false로 루프 종료)
            if (event.changes.none { it.id == first.id && it.pressed }) break
        }
    }
}
