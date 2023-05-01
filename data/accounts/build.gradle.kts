@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library.plus")
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.data.accounts"
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

    implementation(libs.bundles.test.android)
}