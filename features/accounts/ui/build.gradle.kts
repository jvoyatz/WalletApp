@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library.plus")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.org.jetbrains.kotlin.parcelize)
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.features.accounts.ui"
}

dependencies {

    //deps
    implementation(libs.core.ktx)
    implementation(libs.bundles.androidx.ui.common)

    //modules
    implementation(project(":core:ui"))
    implementation(project(":features:accounts:data"))
    implementation(project(":features:accounts:domain"))
    implementation(project(":core:common-android"))
    implementation(project(":core:mvvm_plus"))

    //test
    androidTestImplementation(libs.bundles.test.android)
}