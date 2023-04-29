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

rootProject.name = "Wallet"
include(":app")
include(":core:ui")
include(":core:api")
include(":core:common")
include(":core:mvvm_plus")
include(":core:testing")
include(":core:database")
include(":core:common-android")
include(":domain:accounts")
include(":data:accounts")
include(":features:accounts-list")
include(":features:account-details")