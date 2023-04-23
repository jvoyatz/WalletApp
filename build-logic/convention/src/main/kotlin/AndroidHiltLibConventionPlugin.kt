import com.android.build.gradle.LibraryExtension
import gr.jvoyatz.afse.wallet.configureAndroidCommon
import gr.jvoyatz.afse.wallet.configureAndroidLib
import gr.jvoyatz.afse.wallet.getLibs
import gr.jvoyatz.afse.wallet.getVersionCatalogExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Replaces the plugin `com.android.library' in Android library modules
 */
class AndroidHiltLibConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("afse.wallet.android.library")
                apply("afse.wallet.android.hilt")
            }
        }
    }
}