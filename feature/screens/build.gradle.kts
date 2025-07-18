plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ninecraft.booket.feature.screens"
}

dependencies {
    implementation(projects.core.model)
    api(libs.circuit.runtime)
}
