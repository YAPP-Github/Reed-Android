import com.ninecraft.booket.convention.getLocalProperty

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.retrofit)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.ninecraft.booket.core.ocr"

    defaultConfig {
        buildConfigField("String", "CLOUD_VISION_API_KEY", getLocalProperty("CLOUD_VISION_API_KEY"))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        projects.core.common,
        projects.core.di,

        libs.kotlinx.coroutines.core,
        libs.logger,
    )
}
