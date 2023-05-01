// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://maven.google.com")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN_GRADLE_PLUGIN}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT_GRADLE}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION_GRAPH}")
        classpath("com.google.gms:google-services:${Versions.GOOGLE_SERVICES}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS_GRADLE}")
        classpath("org.jacoco:org.jacoco.core:${Versions.JACOCO}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.BEN_MANES_VERSION_PLUGIN}")
        classpath("com.vanniktech:gradle-maven-publish-plugin:${Versions.GRADLE_MAVEN_PUBLISH}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.10.2")
    }
}
apply(from = "$rootDir/scripts/autoUpdateDeps.gradle.kts")
apply(plugin = "com.github.ben-manes.versions")

ktlint {
    version.set(Versions.KTLINT)
    additionalEditorconfigFile.set(file("./.editorconfig"))
}
plugins {
    id("io.gitlab.arturbosch.detekt").version(Versions.DETEKT)
    id("com.google.devtools.ksp").version(Versions.KSP)
    id("org.jlleitschuh.gradle.ktlint").version(Versions.KTLINT_GRADLE)
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    jacoco
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://maven.google.com")
        maven(url = "https://jitpack.io")
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent
    // Optionally configure plugin
    ktlint {
        disabledRules.set(setOf("no-wildcard-imports"))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    val nonStableKeywords = listOf("rc", "Alpha", "Beta", "SNAPSHOT", "ea", "preview")
    fun isNonStable(version: String): Boolean {
        val containsNonStableKeyword =
            nonStableKeywords.any { version.toUpperCase().contains(it.toUpperCase()) }
        val containsHyphen = version.contains("-")
        return containsNonStableKeyword || containsHyphen
    }

    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Non-stable version")
                }
            }
        }
    }
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")
