import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        jvmMain.dependencies {
            implementation(libs.napier)
            implementation(libs.koin.core)
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(projects.samples.app.samplesAppCommon)
            implementation(compose.desktop.currentOs)
            api(libs.coroutines.swing)
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

compose.desktop {
    application {
        mainClass = "io.github.yanadroidua.kroute.sample.MainKt"
    }
}
