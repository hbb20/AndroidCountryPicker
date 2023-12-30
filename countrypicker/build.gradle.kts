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
        compose = true
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = BuildData.appJavaVersion
        targetCompatibility = BuildData.appJavaVersion
    }

    kotlinOptions {
        jvmTarget = BuildData.appJavaVersion.toString()
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
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
    implementCompose()
    implementEpoxy()
    implementTesting()
}
