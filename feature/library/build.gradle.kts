@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.feature)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ninecraft.booket.feature.library"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementations(
        projects.feature.login,
        projects.feature.settings,

        libs.logger,
    )
}
