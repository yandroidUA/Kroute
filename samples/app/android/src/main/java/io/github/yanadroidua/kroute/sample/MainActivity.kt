package io.github.yanadroidua.kroute.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.yanadroidua.kroute.navigation.router.IRouteExit
import io.github.yanadroidua.kroute.navigation.ui.AndroidApplicationExit
import io.github.yanadroidua.kroute.sample.di.ApplicationRouteExit
import io.github.yanadroidua.kroute.sample.ui.theme.AndoridKrouteSampleTheme
import org.koin.dsl.bind
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SampleApplication.koinApplication.modules(
            modules = module {
                single(ApplicationRouteExit) {
                    AndroidApplicationExit(this@MainActivity)
                } bind IRouteExit::class
            },
        )

        enableEdgeToEdge()

        setContent {
            AndoridKrouteSampleTheme {
                Application()
            }
        }
    }
}
