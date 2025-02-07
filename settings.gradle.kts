rootProject.name = "kroute"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include("test-utils")
include("lint-utils")
include("kroute-navigation-arguments")
include("kroute-navigation-transition")
include("kroute-navigation-router")
include("kroute-navigation-ui")
include("kroute-viewmodel")
include("kroute-koin-viewmodel")
include("kroute-ui-viewmodel")
include("kroute-lifecycle-viewmodel")
include("kroute-lifecycle-koin")
include("kroute-ui-koin")
include("kroute-ui-koin-viewmodel")
include("kroute-ui-navigation-koin-viewmodel")

apply(from = "./samples/samples.build.gradle.kts")
