@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.library.plus")
}

android {
    namespace = "gr.jvoyatz.assignment.wallet.data.accounts"
}

dependencies {

    implementation(project(":core:database"))
    implementation(project(":core:common-android"))
    implementation(project(":core:api"))
    implementation(project(":domain:accounts"))
}