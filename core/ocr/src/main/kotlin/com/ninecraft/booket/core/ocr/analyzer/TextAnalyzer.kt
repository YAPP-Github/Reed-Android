package com.ninecraft.booket.core.ocr.analyzer

import com.google.mlkit.vision.common.InputImage

interface TextAnalyzer {
    fun analyze(inputImage: InputImage)
}
