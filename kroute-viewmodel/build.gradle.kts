import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    id("com.vanniktech.maven.publish")
}

val secretPropsFile = rootProject.file("secret.properties")
val secretProps = Properties()
if (secretPropsFile.exists()) {
    secretProps.load(secretPropsFile.inputStream())
}

group = rootProject.property("GROUP") as String
version = rootProject.property("VERSION") as String

val pomUrl = project.property("POM_URL") as String
val pomLicenseUrl = project.property("POM_LICENSE_URL") as String
val pomLicenseName = project.property("POM_LICENSE_NAME") as String
val pomDeveloperId = System.getenv("POM_DEVELOPER_ID") ?: secretProps["POM_DEVELOPER_ID"] as String
val pomDeveloperName = System.getenv("POM_DEVELOPER_NAME") ?: secretProps["POM_DEVELOPER_NAME"] as String
val pomScmUrl = project.property("POM_SCM_URL") as String
val pomScmConnection = project.property("POM_SCM_CONNECTION") as String
val pomScmDevConnection = project.property("POM_SCM_DEV_CONNECTION") as String

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    jvm {
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.coroutines.core)
            implementation(libs.napier)
            implementation(compose.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.coroutines.test)
            implementation(projects.testUtils)
        }
        jvmMain.dependencies {
            api(libs.coroutines.swing)
        }
    }
}

android {
    namespace = "io.github.yanadroidua.kroute.viewmodel"
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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(artifactId = "viewmodel")
    pom {
        name = "Kroute Viewmodel"
        description = "An extension to the Kroute library to define Viewmodels"
        inceptionYear = "2025"
        url = pomUrl
        licenses {
            license {
                name = pomLicenseName
                url = pomLicenseUrl
                distribution = pomLicenseUrl
            }
        }
        developers {
            developer {
                id = pomDeveloperId
                name = pomDeveloperName
                url = "https://github.com/yandroidua"
            }
        }
        scm {
            url = pomScmUrl
            connection = pomScmConnection
            developerConnection = pomScmDevConnection
        }
    }
}
