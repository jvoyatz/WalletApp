import gr.jvoyatz.afse.wallet.getVersionCatalogExtension
import gr.jvoyatz.afse.wallet.getPackageName

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("afse.wallet.android.application")
    id("afse.wallet.android.hilt")
}

android {
    val packageName = extensions.getVersionCatalogExtension().getPackageName()
    namespace = packageName

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:di"))
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}