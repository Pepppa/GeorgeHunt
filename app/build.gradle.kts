import java.io.File

plugins {
    alias(libs.plugins.android.application)
}

val appVersionName = "1.0"

android {
    namespace = "com.example.georgehunt"
    compileSdk = 36


    defaultConfig {
        applicationId = "com.example.georgehunt"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}



dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

afterEvaluate {
    tasks.named("packageRelease") {
        doLast {
            val releaseDir = file("${projectDir}/release")
            releaseDir.listFiles()?.forEach { apkFile ->
                if (apkFile.name.endsWith(".apk")) {
                    val newFile = File(releaseDir, "george-hunt-$appVersionName-release.apk")
                    val result = apkFile.renameTo(newFile)
                    println(">>> renamed: $result -> ${newFile.name}")
                }
            }
        }
    }
}