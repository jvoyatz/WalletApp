@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library.plus")
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.features.accounts.data"
}

dependencies {

    implementation(project(":core:database"))
    implementation(project(":core:common-android"))
    implementation(project(":core:api"))
    implementation(project(":features:accounts:domain"))

    //implementation(libs.bundles.test.android)
}