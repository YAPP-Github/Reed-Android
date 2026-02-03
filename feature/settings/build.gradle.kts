plugins {
    alias(libs.plugins.booket.android.feature)
    alias(libs.plugins.booket.kotlin.library.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ninecraft.booket.feature.settings"
}

dependencies {
    implementations(
        libs.logger,

        libs.androidx.activity.compose,
    )
}
