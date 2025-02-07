import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ktlint)
}

val composeKtLintVersion = "0.4.16"
val ktlintVersion = "1.3.1"

ktlint {
    android = true // Enable Android-specific linting rules
    ignoreFailures = false // Fail the build if KtLint finds any issues
    version.set(ktlintVersion)
    filter {
        exclude { entry -> entry.file.toString().contains("generated") }
    }
    reporters {
        reporter(ReporterType.HTML) // Output KtLint results in HTML format
    }
    dependencies {
        ktlintRuleset("io.nlopez.compose.rules:ktlint:$composeKtLintVersion")
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<KtlintExtension> {
        android = true // Enable Android-specific linting rules
        ignoreFailures = false // Fail the build if KtLint finds any issues
        version.set(ktlintVersion)
        filter {
            exclude { entry -> entry.file.toString().contains("generated") }
        }
        reporters {
            reporter(ReporterType.HTML) // Output KtLint results in HTML format
        }
    }
    dependencies {
        ktlintRuleset("io.nlopez.compose.rules:ktlint:$composeKtLintVersion")
    }
}

task("addPreCommitGitHookOnBuild") {
    println("⚈ ⚈ ⚈ Running Add Pre Commit Git Hook Script on Build ⚈ ⚈ ⚈")
    exec {
        commandLine("cp", "./.scripts/pre-commit", "./.git/hooks")
    }
    println("✅ Added Pre Commit Git Hook Script.")
}
