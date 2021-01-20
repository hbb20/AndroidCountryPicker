// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-beta03")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.20")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT_GRADLE}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.0.0")
        classpath("com.google.gms:google-services:${Versions.GOOGLE_SERVICES}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS_GRADLE}")
        classpath("org.jacoco:org.jacoco.core:${Versions.JACOCO}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.BEN_MANES_VERSION_PLUGIN}")
        classpath("io.realm:realm-gradle-plugin:${Versions.REALM_GRADLE_PLUGIN}")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.BINTRAY_GRADLE}")
    }
}
apply(plugin = "com.github.ben-manes.versions")
ktlint {
    version.set(Versions.KTLINT)
    additionalEditorconfigFile.set(file("./.editorconfig"))
}
plugins {
    id("io.gitlab.arturbosch.detekt").version(Versions.DETEKT)
    id("org.jlleitschuh.gradle.ktlint").version(Versions.KTLINT_GRADLE)
    jacoco
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven("https://sdk.withpersona.com/android/releases")
        maven("http://kochava.bintray.com/maven")
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
