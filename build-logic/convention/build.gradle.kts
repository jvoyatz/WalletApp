plugins {
    `kotlin-dsl`
}

group = "${libs.versions.packageName}.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    //compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {

    //here we register the custom plugins that will be used throught out main project's modules
    // the main purpose is to move certain dependencies declared in a module in a specific plugin either a library module
    // or a feature module
    //this gives us an extra capability of having single source of truth for the dependencies needed when creating a new module
    plugins {

        register("androidApplication") {
            id = "afse.wallet.android.application"
            implementationClass = "AndroidAppConventionPlugin"
        }
        register("androidKotlin") {
            id = "afse.wallet.jetbrains.kotlin.android"
            implementationClass = "AndroidKotlinConventionPlugin"
        }
        register("androidLibrary") {
            id = "afse.wallet.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "afse.wallet.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
