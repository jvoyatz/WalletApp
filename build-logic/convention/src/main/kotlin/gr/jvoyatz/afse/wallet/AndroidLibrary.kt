package gr.jvoyatz.afse.wallet

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

/**
 * called when we setup an android library module
 */
@Suppress("UnstableApiUsage")
internal fun Project.configureAndroidLib(
    extension: LibraryExtension,
) {
    extension.apply {
        defaultConfig {
            targetSdk = extensions.getVersionCatalogExtension().getTargetSdk()
        }
    }
}