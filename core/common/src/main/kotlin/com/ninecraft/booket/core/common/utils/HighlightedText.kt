package com.ninecraft.booket.core.common.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@SuppressLint("ComposableNaming")
@Composable
fun HighlightedText(
    fullText: String,
    highlightText: String,
    highlightColor: Color,
): AnnotatedString {
    return buildAnnotatedString {
        val startIndex = fullText.indexOf(highlightText)
        if (startIndex != -1) {
            append(fullText.substring(0, startIndex))
            withStyle(style = SpanStyle(color = highlightColor)) {
                append(highlightText)
            }
            append(fullText.substring(startIndex + highlightText.length))
        } else {
            append(fullText)
        }
    }
}
