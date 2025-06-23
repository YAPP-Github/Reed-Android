plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.hilt)
}

android {
    namespace = "com.ninecraft.booket.core.datastore"
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.logger)
}
