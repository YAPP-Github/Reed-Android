plugins {
    alias(libs.plugins.booket.jvm.library)
    alias(libs.plugins.booket.kotlin.library.serialization)
}

dependencies {
    compileOnly(
        libs.compose.stable.marker,
    )
}
