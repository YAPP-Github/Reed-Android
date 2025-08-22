package com.ninecraft.booket.core.ocr.model

import kotlinx.serialization.Serializable

@Serializable
data class CloudVisionResponse(
    val responses: List<AnnotateImageResponse>,
)

@Serializable
data class AnnotateImageResponse(
    val fullTextAnnotation: FullTextAnnotation? = null,
)

@Serializable
data class FullTextAnnotation(
    val text: String? = null,
)
