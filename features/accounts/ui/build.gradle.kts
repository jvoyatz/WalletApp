@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.library")
}

android {
    namespace = "gr.jvoyatz.afse.wallet.features.accounts.ui"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.bundles.androidx.ui.common)
    implementation(project(":core:ui"))
}