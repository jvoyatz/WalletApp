@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library.plus")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.org.jetbrains.kotlin.parcelize)
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.accounts"

    defaultConfig {
        testInstrumentationRunner = "gr.jvoyatz.assignment.wallet.accounts.hilt.HiltTestRunner"
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {

    //deps
    implementation(libs.core.ktx)
    implementation(libs.bundles.androidx.ui.common)

    //modules
    implementation(project(":core:ui"))
    implementation(project(":core:mvvm_plus"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":domain:accounts"))
    implementation(project(":core:testing"))

    //test
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.androidx.navigation)
//  debugApi(libs.fragment.test)
//  testImplementation("androidx.arch.core:core-testing:2.1.0")
}