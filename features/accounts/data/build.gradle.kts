@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.library")
}

android {
    namespace = "gr.jvoyatz.afse.wallet.features.accounts.data"
}

dependencies {

    implementation(project(":core:database"))
    implementation(project(":core:api"))
    implementation(project(":features:accounts:domain"))
}