plugins {
    alias(libs.plugins.booket.android.feature)
}

android {
    namespace = "com.ninecraft.booket.feature.main"
}

dependencies {
    implementations(
        projects.core.di,

        libs.androidx.activity.compose,
        libs.androidx.splash,

        libs.compose.system.ui.controller,

        libs.logger,
    )
}
