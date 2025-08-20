@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.feature)
}

android {
    namespace = "com.ninecraft.booket.feature.splash"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "PACKAGE_NAME", "\"${libs.versions.packageName.get()}\"")
    }
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
