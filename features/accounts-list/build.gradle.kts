@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library.plus")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.org.jetbrains.kotlin.parcelize)
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.accounts"
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
    androidTestImplementation(libs.bundles.androidx.navigation)
    androidTestImplementation(libs.bundles.test.android)
  //  debugApi(libs.fragment.test)
}