@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
}

android {
    namespace = "com.ninecraft.booket.core.ocr"
}

dependencies {
    implementations(
        libs.google.mlkit.text.recognition,
        libs.google.mlkit.text.recognition.korean
    )
}
