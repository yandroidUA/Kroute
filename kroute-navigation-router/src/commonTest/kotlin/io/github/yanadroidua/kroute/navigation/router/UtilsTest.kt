package io.github.yanadroidua.kroute.navigation.router

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import io.github.yanadroidua.kroute.navigation.arguments.navArgument
import io.github.yanadroidua.kroute.navigation.arguments.oneTimeArgument
import io.github.yanadroidua.kroute.navigation.router.utils.TestRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UtilsTest {

    private val oneTimeArgument = oneTimeArgument<Boolean>(key = "one-time")
    private val permanentArgument = navArgument<Int>(key = "permanent")

    @Test
    fun transitionDSL_shouldCreateProperModel() {
        val transition = transition(route = TestRoute.B) {
            putArgument(oneTimeArgument, true)
            putArgument(permanentArgument, 200)
            enterAnimation(enterAnimation = fadeIn())
            currentExitAnimation(exitAnimation = fadeOut())
        }

        assertEquals(2, transition.arguments.size)
        assertTrue { transition.arguments[oneTimeArgument.storageKey] as Boolean }
        assertEquals(200, transition.arguments[permanentArgument.storageKey])
        assertEquals(fadeIn(), transition.enterAnimation)
        assertEquals(fadeOut(), transition.currentExitAnimation)
        assertEquals(TestRoute.B, transition.destination)
    }
}
