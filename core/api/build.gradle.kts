@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.library")
}

android {
    namespace = "gr.jvoyatz.afse.wallet.core.api"
}

dependencies {
    implementation(libs.bundles.networking)
    kapt(libs.moshi.codegen)
}