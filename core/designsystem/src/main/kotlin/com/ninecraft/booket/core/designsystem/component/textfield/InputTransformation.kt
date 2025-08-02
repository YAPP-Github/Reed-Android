package com.ninecraft.booket.core.designsystem.component.textfield

import androidx.compose.foundation.text.input.TextFieldBuffer

/**
 * 숫자만 허용하고, 01, 00 같은 형식을 막는 InputTransformation
 */
val digitOnlyInputTransformation = { text: TextFieldBuffer ->
    val filtered = text.toString().filter { it.isDigit() }

    val transformed = when {
        filtered.isEmpty() -> ""
        filtered == "0" -> "0" // 0 하나만 허용
        filtered.startsWith("0") -> filtered.trimStart('0') // 선행 0 제거
        else -> filtered
    }
    text.replace(0, text.length, transformed)
}
