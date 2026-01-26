@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

import com.ninecraft.booket.convention.getLocalProperty

plugins {
    alias(libs.plugins.booket.android.feature)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ninecraft.booket.feature.login"

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", getLocalProperty("DEBUG_GOOGLE_WEB_CLIENT_ID"))
        }

        getByName("release") {
            buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", getLocalProperty("RELEASE_GOOGLE_WEB_CLIENT_ID"))
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        libs.logger,
        libs.kakao.auth,
        libs.androidx.credentials,
        libs.androidx.credentials.play.services.auth,
        libs.googleid,
    )
}
