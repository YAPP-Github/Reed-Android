import com.ninecraft.booket.convention.Plugins
import com.ninecraft.booket.convention.applyPlugins
import com.ninecraft.booket.convention.implementation
import com.ninecraft.booket.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class KotlinLibrarySerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                Plugins.KOTLINX_SERIALIZATION
            )

            dependencies {
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
