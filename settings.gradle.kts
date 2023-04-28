import org.gradle.api.initialization.resolve.RepositoriesMode.FAIL_ON_PROJECT_REPOS

include(":features:account-details")


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
include(":core:MVVMi")
include(":core:testing")
include(":core:navigation")
include(":core:database")
include(":core:common-android")
include(":domain")
include(":features:accounts:data")
include(":features:accounts:ui")
include(":features:accounts:domain")
