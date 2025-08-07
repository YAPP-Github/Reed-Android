plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.library.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ninecraft.booket.feature.screens"
}

dependencies {
    implementations(
        projects.core.designsystem,
        projects.core.model,

        libs.kotlinx.collections.immutable,

        libs.circuit.foundation,
        libs.compose.shadow,
    )
    api(libs.circuit.runtime)
}
