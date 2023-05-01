plugins {
    id("com.android.library")
    id("com.vanniktech.maven.publish")
    kotlin("android")
    kotlin("kapt")
}

apply(from = "$rootDir/countrypicker/generateCPAndroidResource.gradle.kts")

android {
    defaultConfig {
        compileSdk = BuildData.compileSdkVersion
        minSdk = BuildData.minSdkVersion
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    namespace = "com.hbb20.countrypicker"
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementKotlin(org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION)
    implementation(Deps.appCompat)
    implementation(Deps.constraintLayout)
    implementation(Deps.emoji)
    implementation(Deps.googleMaterialDesign)
    implementation(Deps.timber)
    implementEpoxy()
    implementTesting()
}
