@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ninecraft.booket.feature.main"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementations(
        projects.feature.home,
        projects.feature.library,
        projects.feature.login,
        projects.feature.search,

        libs.kotlinx.collections.immutable,

        libs.androidx.activity.compose,
        libs.androidx.splash,
    )
}
