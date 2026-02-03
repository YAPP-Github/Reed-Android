import com.ninecraft.booket.convention.getLocalProperty


plugins {
    alias(libs.plugins.booket.android.feature)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ninecraft.booket.feature.search"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "REED_KAKAOTALK_CHANNEL_URL", getLocalProperty("REED_KAKAOTALK_CHANNEL_URL"))
    }
}

dependencies {
    implementations(
        libs.kotlinx.collections.immutable,

        libs.logger,
    )
}
