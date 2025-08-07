@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.feature)
}

android {
    namespace = "com.ninecraft.booket.feature.main"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementations(
        libs.androidx.activity.compose,
        libs.androidx.splash,

        libs.compose.system.ui.controller,

        libs.logger,
    )
}
