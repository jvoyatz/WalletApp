@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library")
    alias(libs.plugins.org.jetbrains.kotlin.parcelize)
}

android {
    namespace = "gr.jvoyatz.assignment.core.mvvm.plus"
}

dependencies {
    implementation(libs.coroutines)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.savedState)
    androidTestImplementation(libs.bundles.test.android)
}