@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library")
}

android {
    namespace = "gr.jvoyatz.assignment.core.testing"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.bundles.test.android)
    implementation(libs.bundles.test)
}