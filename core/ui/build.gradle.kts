@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.library")
}

android {
    namespace = "gr.jvoyatz.afse.core.ui"
}

dependencies {
   implementation(libs.core.ktx)
    implementation(libs.bundles.androidx.ui.common)
}