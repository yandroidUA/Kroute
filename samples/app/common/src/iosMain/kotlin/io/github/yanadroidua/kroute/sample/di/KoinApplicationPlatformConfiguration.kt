package io.github.yanadroidua.kroute.sample.di

import io.github.yanadroidua.kroute.navigation.router.IRouteExit
import io.github.yanadroidua.kroute.navigation.ui.IOSApplicationExit
import org.koin.core.KoinApplication
import org.koin.dsl.bind
import org.koin.dsl.module

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class KoinApplicationPlatformConfiguration {
    actual fun KoinApplication.configure() {
        modules(
            module {
                single(ApplicationRouteExit) { IOSApplicationExit() } bind IRouteExit::class
            },
        )
    }
}
