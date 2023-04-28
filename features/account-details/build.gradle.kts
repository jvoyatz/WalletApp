@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library.plus")
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.features.account.details"
}

dependencies {
    //dependencies
    implementation(libs.core.ktx)
    implementation(libs.bundles.androidx.ui.common)

    //core modules
    implementation(project(":core:ui"))
    implementation(project(":core:common-android"))
    implementation(project(":core:mvvm_plus"))

    //unit testing
    implementation(libs.bundles.test)
    //android testing
    implementation(libs.bundles.test.android)
}