import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

const val IMPLEMENTATION = "implementation"
const val DEBUG_IMPLEMENTATION = "debugImplementation"
const val RELEASE_IMPLEMENTATION = "releaseImplementation"
const val TEST_IMPLEMENTATION = "testImplementation"
const val ANDROID_TEST_IMPLEMENTATION = "androidTestImplementation"
const val API = "api"
const val KAPT = "kapt"

object Versions {
    const val BEN_MANES_VERSION_PLUGIN = "0.36.0"
    const val CHUCKER: String = "3.4.0"
    const val COROUTINES = "1.6.4"
    const val DAGGER_2 = "2.29.1" // STOP: 2.30 problem with kaptDebugKotlin
    const val DETEKT = "1.17.1"
    const val EPOXY = "5.1.1"
    const val FIREBASE_CRASHLYTICS_GRADLE = "2.3.0" //2.4.1 causes build error.
    const val GRADLE_BUILD_TOOL = "4.1.1"
    const val GRADLE_MAVEN_PUBLISH = "0.24.0"
    const val GOOGLE_SERVICES = "4.3.3"
    const val JACOCO = "0.8.6"
    const val MOSHI = "1.9.3" // v1.10+ causes build dependency error. needs investigation.
    const val MOSHI_SEALED = "0.2.0"
    const val RETROFIT = "2.9.0"
    const val NAVIGATION_GRAPH = "2.5.3"
    const val SCARLET_VERSION = "0.1.11"
    const val KOTLIN = "1.4.21"
    const val KTLINT_GRADLE = "9.4.1"
    const val KTLINT = "0.40.0"
    const val REALM_GRADLE_PLUGIN = "10.2.0"
    const val ROOM = "2.2.6"
    const val BINTRAY_GRADLE = "1.8.5"

    // Linked Dependencies
    // COMPOSE_KOTLIN_COMPILER <- KOTLIN_GRADLE_PLUGIN <- KSP
    // KSP can only be updated to compatible version of KOTLIN_GRADLE_PLUGIN and
    // KOTLIN_GRADLE_PLUGIN can only be updated to compatible version of COMPOSE_KOTLIN_COMPILER
    // This means when COMPOSE_KOTLIN_COMPILER is updated, we should update KOTLIN_GRADLE_PLUGIN and KSP
    //
    // Check and update latest version of COMPOSE_COMPILER and it's compatible KOTLIN_GRADLE_PLUGIN at:
    // https://developer.android.com/jetpack/androidx/releases/compose-kotlin
    //
    // Once KOTLIN_GRADLE_PLUGIN is updated, find and update latest but compatible KSP version.
    // Release versions can be found here:
    // https://github.com/google/ksp/releases
    // First half of KSP version shows compatible KOTLIN version,
    // For example, ksp version 1.8.0-1.0.9 means it's compatible with KOTLIN 1.8.0
    const val COMPOSE_COMPILER = "1.4.3"
    const val KOTLIN_GRADLE_PLUGIN = "1.8.10" // STOP : See 'Linked Dependencies' comment
    const val KSP = "1.8.10-1.0.9" // STOP : See 'Linked Dependencies' comment
}

object Deps {
    const val activityKtx = "androidx.activity:activity-ktx:1.2.0-rc01"
    const val appCompat = "androidx.appcompat:appcompat:1.6.1"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val emoji = "androidx.emoji:emoji:1.1.0"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.5.5"
    const val googleMaterialDesign = "com.google.android.material:material:1.9.0-alpha02"
    const val viewModels = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val apacheCSV = "org.apache.commons:commons-csv:1.7"
}

fun DependencyHandlerScope.implementProject(moduleName: String) {
    add(IMPLEMENTATION, project(":$moduleName"))
}

fun DependencyHandlerScope.apiProject(moduleName: String) {
    add(API, project(":$moduleName"))
}

fun DependencyHandlerScope.implementArchitectureComponents() {
    add(IMPLEMENTATION, "androidx.navigation:navigation-fragment:${Versions.NAVIGATION_GRAPH}")
    add(IMPLEMENTATION, "androidx.navigation:navigation-ui:${Versions.NAVIGATION_GRAPH}")
    add(IMPLEMENTATION, "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION_GRAPH}")
    add(IMPLEMENTATION, "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION_GRAPH}")
    add(IMPLEMENTATION, "androidx.lifecycle:lifecycle-extensions:2.2.0")
    add(IMPLEMENTATION, "androidx.legacy:legacy-support-v4:1.0.0")
    add(IMPLEMENTATION, "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    add(IMPLEMENTATION, "androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    add(IMPLEMENTATION, "com.github.hadilq.liveevent:liveevent:1.2.0")
}

fun DependencyHandlerScope.implementEpoxy() {
    add(IMPLEMENTATION, "com.airbnb.android:epoxy:${Versions.EPOXY}")
    add(KAPT, "com.airbnb.android:epoxy-processor:${Versions.EPOXY}")
}

fun DependencyHandlerScope.implementKotlin(version: String) {
    add(IMPLEMENTATION, kotlin("stdlib-jdk7", version))
    add(IMPLEMENTATION, "androidx.core:core-ktx:1.9.0")
}

fun DependencyHandlerScope.implementTesting() {
    add(ANDROID_TEST_IMPLEMENTATION, "androidx.test.ext:junit:1.1.5")
    add(ANDROID_TEST_IMPLEMENTATION, "androidx.test.espresso:espresso-core:3.5.1")
    add(ANDROID_TEST_IMPLEMENTATION, "androidx.test:core:1.3.0")


    // https://github.com/robolectric/robolectric/issues/5848 Will be able to update to 4.4 only after mockk can be updated to > 1.10.0
    add(TEST_IMPLEMENTATION, "org.robolectric:robolectric:4.9.2")

    add(TEST_IMPLEMENTATION, "androidx.arch.core:core-testing:2.2.0")
    add(TEST_IMPLEMENTATION, "androidx.test:core:1.5.0")
    add(TEST_IMPLEMENTATION, "androidx.test:core-ktx:1.5.0")
    add(TEST_IMPLEMENTATION, "io.mockk:mockk:1.13.4")
    add(TEST_IMPLEMENTATION, "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}")
    add(TEST_IMPLEMENTATION, "junit:junit:4.13.2")
}
