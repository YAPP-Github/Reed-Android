@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.feature)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ninecraft.booket.feature.record"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementations(
        projects.core.ocr,

        libs.compose.system.ui.controller,

        libs.androidx.activity.compose,
        libs.androidx.camera.camera2,
        libs.androidx.camera.lifecycle,
        libs.androidx.camera.view,

        libs.logger,
    )
}
