package io.github.yanadroidua.kroute.sample.di

import org.koin.core.KoinApplication

expect class KoinApplicationPlatformConfiguration {

    fun KoinApplication.configure()
}
