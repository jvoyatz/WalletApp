@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library.plus")
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.data.accounts"

    System.out.println(this.defaultConfig.testInstrumentationRunner)
}

dependencies {

    //core
    //sources
    implementation(project(":core:api"))
    implementation(project(":core:database"))
    //common
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    //domain
    implementation(project(":domain:accounts"))

    //testing
    testImplementation(libs.bundles.test)
    testImplementation(project(":core:testing"))

    //android testing
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(libs.room.testing)
}