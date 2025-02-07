package io.github.yanadroidua.kroute.sample

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.yanadroidua.kroute.sample.di.KoinApplicationPlatformConfiguration
import io.github.yanadroidua.kroute.sample.di.initKoin
import org.koin.core.KoinApplication

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        koinApplication = initKoin(platformConfiguration = KoinApplicationPlatformConfiguration(application = this))
    }

    companion object {
        lateinit var koinApplication: KoinApplication
    }
}
