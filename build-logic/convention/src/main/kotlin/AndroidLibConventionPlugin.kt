import com.android.build.gradle.LibraryExtension
import gr.jvoyatz.assignment.wallet.configureAndroidCommon
import gr.jvoyatz.assignment.wallet.configureAndroidLib
import gr.jvoyatz.assignment.wallet.getLibs
import gr.jvoyatz.assignment.wallet.getVersionCatalogExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Replaces the plugin `com.android.library' in Android library modules
 */
class AndroidLibConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.kapt")
            }


            extensions.configure<LibraryExtension> {
                configureAndroidCommon(this)
                configureAndroidLib(this)
            }

            val libs = extensions.getVersionCatalogExtension().getLibs()
            target.dependencies {
                "implementation"(target.project(":core:common"))
                "implementation"(libs.findLibrary("logging.timber").get())
                "implementation"(libs.findLibrary("coroutines").get())
                "implementation"(libs.findLibrary("javax.inject").get())
                "testImplementation"(libs.findBundle("test").get())
            }


        }
    }
}
