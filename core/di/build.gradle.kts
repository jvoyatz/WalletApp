@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.library.plus")
}

android {
    namespace = "gr.jvoyatz.afse.core.di"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.fragment)
    implementation(libs.navigation.fragment)
    implementation(project(":core:navigation"))
    implementation(project(":core:common-android"))
    implementation(project(":core:api"))
    implementation(project(":core:database"))
}