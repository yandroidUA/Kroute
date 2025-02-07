package io.github.yanadroidua.kroute.sample.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class KoinApplicationPlatformConfiguration(
    private val application: Application,
) {

    actual fun KoinApplication.configure() {
        androidContext(application)
        androidLogger()
    }
}
