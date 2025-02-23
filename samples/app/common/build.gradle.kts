import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    jvm {
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.animation)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(libs.napier)
            implementation(projects.lintUtils)
            api(projects.krouteViewmodel)
            api(projects.krouteUiViewmodel)
            api(projects.krouteNavigationRouter)
            api(projects.krouteUiKoin)
            api(projects.krouteLifecycleKoin)
            api(projects.krouteLifecycleViewmodel)
            api(projects.krouteNavigationArguments)
            api(projects.krouteNavigationTransition)
            api(projects.krouteNavigationUi)
            api(projects.krouteKoinViewmodel)
            api(projects.krouteUiKoinViewmodel)
            api(projects.krouteUiNavigationKoinViewmodel)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.activity.compose)
        }
    }
}

android {
    namespace = "io.github.yanadroidua.kroute.sample.android"
    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
        compileSdk = libs.versions.android.sdk.compile.get().toInt()
    }
}
