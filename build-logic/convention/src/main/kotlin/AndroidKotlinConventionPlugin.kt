import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import gr.jvoyatz.afse.wallet.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Here we check the type of the plugin used, and
 * we device whether we should configure Kotlin stuff
 */
class AndroidKotlinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
            }

            /**
             * in android apps we have access to ApplicationExtension
             */
            extensions.findByType(ApplicationExtension::class.java)?.apply {
                configureKotlin(this)
            }

            /**
             * to the rest modules (Library modules), we have access to LibraryExtension
             */
            extensions.findByType(LibraryExtension::class.java)?.apply {
                configureKotlin(this)
            }
        }
    }
}