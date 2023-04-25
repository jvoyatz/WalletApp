import org.gradle.api.initialization.resolve.RepositoriesMode.FAIL_ON_PROJECT_REPOS

pluginManagement {
    repositories {
        includeBuild("build-logic")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AFSE Wallet"
include(":app")
include(":core:ui")
include(":core:di")
include(":core:api")
include(":core:common")
include(":core:MVVMi")
include(":core:testing")
include(":core:navigation")
include(":core:database")
