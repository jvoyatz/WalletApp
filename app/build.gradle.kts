import gr.jvoyatz.assignment.wallet.getPackageName
import gr.jvoyatz.assignment.wallet.getVersionCatalogExtension


@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("assignment.wallet.android.application")
    id("assignment.wallet.android.hilt")
    id("androidx.navigation.safeargs.kotlin")
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

    packagingOptions {
        resources.merges.add("META-INF/gradle/incremental.annotation.processors")
        resources.merges.add("META-INF/LICENSE.md")
        resources.merges.add("META-INF/LICENSE-notice.md")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    //android dependencies
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.core.ktx)

    //core modules
    implementation(project(":core:ui"))
    implementation(project(":core:common-android"))

    //features
    implementation(project(":features:accounts:ui"))
    implementation(project(":features:account-details"))

//
//    implementation(libs.appcompat)
//    implementation(libs.material)
//    implementation(libs.constraintlayout)
}