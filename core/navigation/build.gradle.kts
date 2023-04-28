@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library.plus")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "gr.jvoyatz.assignment.core.navigation"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:common-android"))
    implementation(libs.core.ktx)
    implementation(libs.fragment)
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.javax.inject)
    testImplementation(libs.junit)

    implementation(project(":features:accounts:ui"))
    implementation(project(":features:account-details"))
}