import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {

    defaultConfig {
        compileSdk = BuildData.compileSdkVersion
        minSdk = BuildData.minSdkVersion
        targetSdk = BuildData.targetSdkVersion
        applicationId = BuildData.demoApplicationId
        versionCode = BuildData.versionCode
        versionName = BuildData.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    packagingOptions {
        jniLibs {
            excludes += setOf("META-INF/licenses/**")
        }
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/licenses/**",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/AL2.0",
                "META-INF/*.kotlin_module",
                "META-INF/LGPL2.1",
                "**/attach_hotspot_windows.dll"
            )
        }
    }
    namespace = "com.hbb20.androidcountrypicker"
    lint {
        abortOnError = false
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
    implementProject("flagpack1")
}
