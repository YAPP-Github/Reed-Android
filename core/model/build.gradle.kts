plugins {
    alias(libs.plugins.booket.jvm.library)
}

dependencies {
    compileOnly(
        libs.compose.stable.marker,
    )
}
