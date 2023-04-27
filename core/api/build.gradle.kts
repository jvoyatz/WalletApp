import java.io.File
import java.io.FileInputStream
import java.util.*

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("assignment.wallet.android.library")
}

val prop = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}
println("Property:" + prop.getProperty("propertyName"))

android {
    namespace = "gr.jvoyatz.assignment.wallet.core.api"

    defaultConfig {
        buildConfigField("String", "HOST", prop.getProperty("HOST"))
        buildConfigField("String", "USERNAME", prop.getProperty("USERNAME"))
        buildConfigField("String", "PASSWORD", prop.getProperty("PASSWORD"))
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