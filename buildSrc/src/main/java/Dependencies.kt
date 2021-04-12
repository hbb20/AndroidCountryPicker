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
    const val COROUTINES = "1.4.2"
    const val DAGGER_2 = "2.29.1" // STOP: 2.30 problem with kaptDebugKotlin
    const val DETEKT = "1.14.2"
    const val EPOXY =
        "4.1.0" //4.2.0 causes error running gradle task :app:checkDebugDuplicateClasses https://github.com/airbnb/epoxy/releases/tag/4.2.0
    const val FIREBASE_CRASHLYTICS_GRADLE = "2.3.0" //2.4.1 causes build error.
    const val GRADLE_BUILD_TOOL = "4.1.1"
    const val GOOGLE_SERVICES =
        "4.3.3" // STOP: 4.3.4 does not pick release/google-services.json for Release Build
    // Follow https://github.com/google/play-services-plugins/issues/163
    // to confirm working, run Release build variant and make sure "Firebase Development" is *not* on Profile Tab footer

    const val HYPERION = "0.9.30"
    const val JACOCO = "0.8.6"
    const val MOSHI = "1.9.3" // v1.10+ causes build dependency error. needs investigation.
    const val MOSHI_SEALED = "0.2.0"
    const val RETROFIT = "2.9.0"
    const val NAVIGATION_GRAPH = "2.3.2"
    const val SCARLET_VERSION = "0.1.11"
    const val KOTLIN = "1.4.21"
    const val KOTLIN_GRADLE = "1.4.21"
    const val KTLINT_GRADLE = "9.4.1"
    const val KTLINT = "0.40.0"
    const val REALM_GRADLE_PLUGIN = "10.2.0"
    const val ROOM = "2.2.6"
    const val BINTRAY_GRADLE = "1.8.5"
}

object Deps {
    const val activityKtx = "androidx.activity:activity-ktx:1.2.0-rc01"
    const val appCompat = "androidx.appcompat:appcompat:1.2.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val emoji = "androidx.emoji:emoji:1.1.0"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.2.5"
    const val googleMaterialDesign = "com.google.android.material:material:1.3.0-beta01"
    const val viewModels = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val apacheCSV = "org.apache.commons:commons-csv:1.7"
}

fun DependencyHandlerScope.implementProject(moduleName: String) {
    add(IMPLEMENTATION, project(":$moduleName"))
}

fun DependencyHandlerScope.implementArchitectureComponents() {
    add(IMPLEMENTATION, "androidx.navigation:navigation-fragment:${Versions.NAVIGATION_GRAPH}")
    add(IMPLEMENTATION, "androidx.navigation:navigation-ui:${Versions.NAVIGATION_GRAPH}")
    add(IMPLEMENTATION, "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION_GRAPH}")
    add(IMPLEMENTATION, "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION_GRAPH}")
    add(IMPLEMENTATION, "androidx.lifecycle:lifecycle-extensions:2.2.0")
    add(IMPLEMENTATION, "androidx.legacy:legacy-support-v4:1.0.0")
    add(IMPLEMENTATION, "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    add(IMPLEMENTATION, "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    add(IMPLEMENTATION, "com.github.hadilq.liveevent:liveevent:1.2.0")
}

fun DependencyHandlerScope.implementEpoxy() {
    add(IMPLEMENTATION, "com.airbnb.android:epoxy:${Versions.EPOXY}")
    add(KAPT, "com.airbnb.android:epoxy-processor:${Versions.EPOXY}")
}

fun DependencyHandlerScope.implementKotlin(version: String) {
    add(IMPLEMENTATION, kotlin("stdlib-jdk7", version))
    add(IMPLEMENTATION, "androidx.core:core-ktx:1.3.2")
}

fun DependencyHandlerScope.implementTesting() {
    add(ANDROID_TEST_IMPLEMENTATION, "androidx.test.ext:junit:1.1.2")
    add(ANDROID_TEST_IMPLEMENTATION, "androidx.test.espresso:espresso-core:3.3.0")

    // https://github.com/robolectric/robolectric/issues/5848 Will be able to update to 4.4 only after mockk can be updated to > 1.10.0
    add(TEST_IMPLEMENTATION, "org.robolectric:robolectric:4.4")

    add(TEST_IMPLEMENTATION, "androidx.arch.core:core-testing:2.1.0")
    add(TEST_IMPLEMENTATION, "androidx.test:core:1.3.0")
    add(TEST_IMPLEMENTATION, "androidx.test:core-ktx:1.3.0")
    add(TEST_IMPLEMENTATION, "io.mockk:mockk:1.10.6")
    add(TEST_IMPLEMENTATION, "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}")
    add(TEST_IMPLEMENTATION, "junit:junit:4.13.1")
}
