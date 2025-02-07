package io.github.yanadroidua.kroute.sample.di

import org.koin.core.context.startKoin

fun initKoin(
    platformConfiguration: KoinApplicationPlatformConfiguration,
) = startKoin {
    with(platformConfiguration) { this@startKoin.configure() }
    modules(applicationModule())
}
