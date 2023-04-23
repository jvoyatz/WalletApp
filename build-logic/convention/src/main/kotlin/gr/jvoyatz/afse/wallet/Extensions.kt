package gr.jvoyatz.afse.wallet

import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.getByType


/**
 * Extension methods to ease the access of versions defined in
 * the toml file
 */


fun ExtensionContainer.getVersionCatalogExtension() = this.getByType<VersionCatalogsExtension>()


val ID_PREFIX = "afse.wallet"

private const val LIBS = "libs"
private const val PACKAGE_NAME = "packageName"
private const val VERSION_NAME = "versionName"
private const val VERSION_CODE = "versionCode"
private const val TARGET_SDK = "targetSdk"
private const val MIN_SDK = "minSdk"
private const val COMPILE_SDK = "compileSdk"

fun VersionCatalogsExtension.getLibs() = named("libs")
fun VersionCatalogsExtension.getPackageName(): String = getLibs().findVersion(PACKAGE_NAME).get().toString()
fun VersionCatalogsExtension.getTargetSdk() = getLibs().findVersion(TARGET_SDK).get().toString().toInt()
fun VersionCatalogsExtension.getCompileSdk() = getLibs().findVersion(COMPILE_SDK).get().toString().toInt()
fun VersionCatalogsExtension.getMinSdk():Int = getLibs().findVersion(MIN_SDK).get().toString().toInt()
fun VersionCatalogsExtension.getVersionName() = getLibs().findVersion(VERSION_NAME).get().toString()
fun VersionCatalogsExtension.getVersionCode() = getLibs().findVersion(VERSION_CODE).get().toString().toInt()