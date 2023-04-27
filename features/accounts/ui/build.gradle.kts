@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library")
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.features.accounts.ui"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.bundles.androidx.ui.common)
    implementation(project(":core:ui"))
    androidTestImplementation(libs.bundles.test.android)
}