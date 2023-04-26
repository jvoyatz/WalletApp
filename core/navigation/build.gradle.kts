@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.library")
}

android {
    namespace = "gr.jvoyatz.afse.core.navigation"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.fragment)
    implementation(libs.javax.inject)
    testImplementation(libs.junit)
}