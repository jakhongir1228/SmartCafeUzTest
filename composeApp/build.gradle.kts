import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.reaktive)
            implementation(libs.kotlinx.serialization.runtime.common)
            implementation(libs.kotlinx.serialization.runtime)
            implementation(libs.androidx.recyclerview)
            implementation(libs.androidx.swiperefreshlayout)
            implementation(libs.picasso)
            implementation(libs.androidx.core.ktx)
            implementation(libs.kotlin.stdlib.jdk8)
            implementation(libs.androidx.annotation)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.fragment.ktx)
            implementation(libs.androidx.lifecycle.viewmodel.ktx)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.lifecycle.viewmodel.savedstate)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.material)
            implementation(libs.converter.gson)
            implementation(libs.gson)

            // for retrofit
            implementation(libs.retrofit)
            implementation(libs.converter.gson)
            implementation(libs.adapter.rxjava2)
            implementation(libs.okhttp)
            implementation(libs.okhttp.urlconnection)
            implementation(libs.logging.interceptor)

            //chuck interceptor for Network Debuggin
            implementation(libs.library)
            implementation(libs.library.no.op)

            //mviKotlin
            implementation(libs.mvikotlin)

            //dagger
            implementation(libs.dagger)
        }
    }
}

android {
    namespace = "uz.smartcafe"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "uz.smartcafe"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

