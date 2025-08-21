package com.ninecraft.booket.core.ocr.model

import kotlinx.serialization.Serializable

@Serializable
data class CloudVisionRequest(
    val requests: List<AnnotateImageRequest>,
)

@Serializable
data class AnnotateImageRequest(
    val image: VisionImage,
    val features: List<Feature>,
    val imageContext: ImageContext? = null,
)

@Serializable
data class VisionImage(
    val content: String,
)

@Serializable
data class Feature(
    val type: String = "TEXT_DETECTION",
)

@Serializable
data class ImageContext(
    val languageHints: List<String>? = null,
)
