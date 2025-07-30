plugins {
    alias(libs.plugins.booket.android.library)
}

android {
    namespace = "com.ninecraft.booket.core.datastore.api"
}

dependencies {
    implementations(
        projects.core.model,

        libs.kotlinx.coroutines.core
    )
}
