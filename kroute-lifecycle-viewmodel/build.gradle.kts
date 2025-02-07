import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.napier)
            implementation(compose.runtime)
            implementation(projects.krouteNavigationRouter)
            implementation(projects.krouteViewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.coroutines.test)
            implementation(projects.testUtils)
        }
    }
}

android {
    namespace = "com.yanadroid.kroute.lifecycle.viewmodel"
    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
        compileSdk = libs.versions.android.sdk.compile.get().toInt()
    }
}
