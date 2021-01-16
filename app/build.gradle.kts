import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {

    defaultConfig {
        compileSdkVersion(BuildData.compileSdkVersion)
        minSdkVersion(BuildData.minSdkVersion)
        targetSdkVersion(BuildData.targetSdkVersion)
        applicationId = BuildData.demoApplicationId
        versionCode = BuildData.versionCode
        versionName = BuildData.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    lintOptions {
        isAbortOnError = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/licenses/**")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/AL2.0")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/LGPL2.1")
        exclude("**/attach_hotspot_windows.dll")
    }
}

// Epoxy needs this
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementKotlin(KotlinCompilerVersion.VERSION)
    implementation(Deps.appCompat)
    implementation(Deps.constraintLayout)
    implementation(Deps.fragmentKtx)
    implementation(Deps.googleMaterialDesign)
    implementation(Deps.viewModels)
    implementation(Deps.timber)
    implementArchitectureComponents()
    implementEpoxy()
    implementTesting()
    implementProject("countrypicker")
}
