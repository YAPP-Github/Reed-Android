@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties


plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.retrofit)
    alias(libs.plugins.booket.android.hilt)
}

android {
    namespace = "com.ninecraft.booket.core.ocr"

    defaultConfig {
        buildConfigField("String", "CLOUD_VISION_API_KEY", getApiKey("CLOUD_VISION_API_KEY"))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        projects.core.common,

        libs.logger,
        libs.androidx.camera.core,

        libs.google.mlkit.text.recognition.korean,
    )
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
