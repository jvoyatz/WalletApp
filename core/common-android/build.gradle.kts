@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library")
    alias(libs.plugins.org.jetbrains.kotlin.parcelize)
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.common.android"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.javax.inject)

    implementation(project(":core:common"))
    implementation(project(":domain:accounts"))
}