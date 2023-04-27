@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.library")
}

android {
    namespace = "gr.jvoyatz.afse.wallet.core.api"

    defaultConfig {
        buildConfigField("String", "HOST", "\"http://ktor-env.eba-asssfhm8.eu-west-1.elasticbeanstalk.com/\"")
        buildConfigField("String", "USERNAME", "\"Advantage\"")
        buildConfigField("String", "PASSWORD", "\"mobileAssignment\"")
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:testing"))
    implementation(project(":core:common-android"))
    implementation(libs.bundles.networking)
    kapt(libs.moshi.codegen)
}