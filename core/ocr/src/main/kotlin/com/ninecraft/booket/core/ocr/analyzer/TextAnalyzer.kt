package com.ninecraft.booket.core.ocr.analyzer

import androidx.camera.core.ImageProxy

interface TextAnalyzer {
    fun analyze(imageProxy: ImageProxy)
}
