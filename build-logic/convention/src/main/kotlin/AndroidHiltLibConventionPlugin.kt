import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Replaces the plugin `com.android.library' in Android library modules
 */
class AndroidHiltLibConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("assignment.wallet.android.library")
                apply("assignment.wallet.android.hilt")
            }
        }
    }
}