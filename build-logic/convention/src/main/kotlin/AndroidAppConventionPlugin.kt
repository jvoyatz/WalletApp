import com.android.build.api.dsl.ApplicationExtension
import gr.jvoyatz.assignment.wallet.configureAndroidApp
import gr.jvoyatz.assignment.wallet.configureAndroidCommon
import gr.jvoyatz.assignment.wallet.getLibs
import gr.jvoyatz.assignment.wallet.getVersionCatalogExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Removing certain piece of code from the app's module gradle file.
 *
 * [minSdk etc].
 *
 * An instance of the class declared in this file will re place
 * the Android Application plugin in the app/build.gradle.kts.
 *
 */
class AndroidAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            //Since this file will be used in the gradle file for the app module,
            //we already know that we use the ApplicationExtension which
            //stands for `Extension for the Android Gradle Plugin Application plugin.`
            extensions.configure<ApplicationExtension> {
                configureAndroidCommon(this)
                configureAndroidApp(this)
            }

            val libs = extensions.getVersionCatalogExtension().getLibs()
            target.dependencies {
                "implementation"(libs.findLibrary("logging.timber").get())
            }
        }
    }
}