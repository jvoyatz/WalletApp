@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.kotlin")
}

dependencies{
    implementation(project(":core:common"))
    implementation(libs.javax.inject)
}