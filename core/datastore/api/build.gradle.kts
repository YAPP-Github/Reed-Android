plugins {
    alias(libs.plugins.booket.android.library)
}

android {
    namespace = "com.ninecraft.booket.core.datastore.api"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
