import com.ninecraft.booket.convention.getLocalProperty

plugins {
    alias(libs.plugins.booket.android.feature)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ninecraft.booket.feature.login"

    defaultConfig {
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", getLocalProperty("GOOGLE_WEB_CLIENT_ID"))
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
