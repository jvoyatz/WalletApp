@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.library")
}

android {
    namespace = "gr.jvoyatz.afse.core.database"

    packagingOptions {
        resources.merges.add("META-INF/gradle/incremental.annotation.processors")
        resources.merges.add("META-INF/LICENSE.md")
        resources.merges.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    implementation(libs.bundles.room)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)

    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test)
}