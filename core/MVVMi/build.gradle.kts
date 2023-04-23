@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.library")
    alias(libs.plugins.org.jetbrains.kotlin.parcelize)
}

android {
    namespace = "gr.jvoyatz.afse.core.mvvmi"
}

dependencies {
    implementation(libs.coroutines)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.savedState)
}