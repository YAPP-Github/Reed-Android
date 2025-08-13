@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.feature)
}

android {
    namespace = "com.ninecraft.booket.feature.splash"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementations(
        libs.compose.system.ui.controller,
        libs.logger,
    )
}
