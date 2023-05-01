// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.org.jetbrains.kotlin.parcelize) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    alias(libs.plugins.com.android.library) apply false
}

buildscript {
    dependencies {
        val navVersion = "2.5.3"

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${navVersion}")
    }
}

apply(from = "gradle/projectDependencyGraph.gradle")

true // Needed to make the Suppress annotation work for the plugins block