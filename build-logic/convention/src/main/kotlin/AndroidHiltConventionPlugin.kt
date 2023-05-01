import gr.jvoyatz.assignment.wallet.getLibs
import gr.jvoyatz.assignment.wallet.getVersionCatalogExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("dagger.hilt.android.plugin")
                apply("org.jetbrains.kotlin.kapt")
            }

            extensions.getVersionCatalogExtension().getLibs().let{
                target.dependencies {
                    "implementation"(it.findLibrary("dagger.hilt.android").get())
                    "kapt"(it.findLibrary("dagger.hilt.compiler").get())
                    "kaptAndroidTest"(it.findLibrary("dagger.hilt.compiler").get())
                    "androidTestImplementation"(it.findLibrary("dagger.hilt.testing").get())
                }
            }
        }
    }
}