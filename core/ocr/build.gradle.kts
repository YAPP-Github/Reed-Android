@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.retrofit)
    alias(libs.plugins.booket.android.hilt)
}

android {
    namespace = "com.ninecraft.booket.core.ocr"
}

dependencies {
    implementations(
        libs.logger,
        libs.androidx.camera.core,

        libs.google.mlkit.text.recognition.korean,
    )
}
