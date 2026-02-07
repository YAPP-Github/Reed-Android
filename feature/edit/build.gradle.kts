plugins {
    alias(libs.plugins.booket.android.feature)
    alias(libs.plugins.booket.kotlin.library.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ninecraft.booket.feature.edit"
}

dependencies {
    implementations(
        libs.kotlinx.collections.immutable,

        libs.logger,
    )
}
