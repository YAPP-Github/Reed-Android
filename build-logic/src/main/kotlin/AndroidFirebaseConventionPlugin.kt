
import com.ninecraft.booket.convention.Plugins
import com.ninecraft.booket.convention.applyPlugins
import com.ninecraft.booket.convention.implementation
import com.ninecraft.booket.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                Plugins.GOOGLE_SERVICES,
                Plugins.FIREBASE_CRASHLYTICS,
            )

            dependencies {
                implementation(platform(libs.firebase.bom))
                implementation(libs.firebase.analytics)
                implementation(libs.firebase.crashlytics)
            }
        }
    }
}
