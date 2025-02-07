package io.github.yanadroidua.kroute.navigation.router

import androidx.compose.runtime.Immutable
import io.github.aakira.napier.Napier
import io.github.yanadroidua.kroute.navigation.arguments.Argument
import io.github.yanadroidua.kroute.navigation.transition.Transition
import io.github.yanadroidua.kroute.navigation.transition.TransitionBuilder
import io.github.yanadroidua.kroute.navigation.transition.scope.TransitionScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Immutable
internal class ScreenRouterImpl<R : Any>(
    initialDestination: R,
    private val routeExit: IRouteExit,
) : IScreenRouter<R> {

    private val route: MutableStateFlow<Transition<R>>

    /**
     * Do not modify by yourself, it's not thread safe, please use according function. It's internal only for
     * testing purposes.
     */
    internal val stack: MutableList<Transition<R>>

    init {
        val initialTransition = TransitionBuilder.build(destination = initialDestination)
        route = MutableStateFlow(initialTransition)
        stack = mutableListOf(initialTransition)
    }

    private val mutex = Mutex()
    private val listeners = mutableSetOf<IStackListener<R>>()

    override val currentTransition: StateFlow<Transition<R>> = route.asStateFlow()

    override val isPerformingOperation: Boolean = mutex.isLocked

    private val operationChannel = Channel<IOngoingOperation<R>>(
        capacity = Channel.RENDEZVOUS,
        onBufferOverflow = BufferOverflow.SUSPEND,
    )

    private fun <T> MutableList<T>.replaceLast(item: T) {
        removeAt(lastIndex)
        add(item)
    }

    override val ongoingOperation: ReceiveChannel<IOngoingOperation<R>> = operationChannel

    override suspend fun back(configuration: TransitionScope.() -> Unit): Boolean {
        if (!mutex.tryLock()) {
            Napier.w(tag = TAG, message = "Failed to acquire a mutex, dropping back operation")
            // there is some operation in place
            return false
        }

        Napier.d(tag = TAG, message = "Popping back")
        var popped: Transition<R>? = null

        if (stack.size > 0) {
            popped = stack.removeAt(stack.lastIndex)
        }

        // TODO here is the issue that configuration has not been propagated to the onExit, I should change that
        if (stack.isEmpty()) {
            popped?.let {
                listeners.forEach { it.onPopped(listOf(popped), emptyList()) }
            }
            routeExit.onExit(configuration)
            mutex.unlock()
            return false
        }

        val nextTransition = TransitionBuilder(stack.last()).apply(configuration).build()
        stack.replaceLast(nextTransition)

        listeners.forEach { it.onPopped(popped?.run(::listOf) ?: emptyList(), stack.toMutableList()) }

        if (nextTransition.expedite) {
            this.route.value = nextTransition
        } else {
            operationChannel.send(BackwardOngoingOperation(nextTransition))
        }

        mutex.unlock()
        return true
    }

    override suspend fun isInStack(route: R): Boolean = mutex.withLock {
        stack.any { it.destination == route }
    }

    override suspend fun replaceAll(route: R, configuration: TransitionScope.() -> Unit) {
        if (!mutex.tryLock()) {
            Napier.w(tag = TAG, message = "Failed to acquire a mutex, replaceAll operation")
            // there is some operation in place
            return
        }

        Napier.d(tag = TAG, message = "Replacing with $route")

        val oldStack = stack.toMutableList()
        stack.clear()
        listeners.forEach { it.onPopped(oldStack, emptyList()) }

        val newTransition = TransitionBuilder(route).apply(configuration).build()

        stack.add(newTransition)
        listeners.forEach { it.onPushed(newTransition, stack.toMutableList()) }

        if (newTransition.expedite) {
            this.route.value = newTransition
        } else {
            operationChannel.send(ForwardOngoingOperation(newTransition))
        }

        Napier.d(tag = TAG, message = "Unlocking mutex - replaceAll")

        mutex.unlock()
    }

    override suspend fun replaceAllWithBackStack(
        route: R,
        backStack: List<Transition<R>>,
        configuration: TransitionScope.() -> Unit,
    ) {
        if (!mutex.tryLock()) {
            Napier.w(tag = TAG, message = "Failed to acquire a mutex, replaceAll operation")
            // there is some operation in place
            return
        }

        Napier.d(tag = TAG, message = "Replacing all with the back stack, $route, backStack size: ${backStack.size}")

        val oldStack = stack.toMutableList()
        stack.clear()
        listeners.forEach { it.onPopped(oldStack, emptyList()) }

        backStack.forEach { backStackTransition ->
            stack.add(backStackTransition)
            listeners.forEach { it.onPushed(backStackTransition, stack.toMutableList()) }
        }

        val newTransition = TransitionBuilder(route).apply(configuration).build()

        stack.add(newTransition)
        listeners.forEach { it.onPushed(newTransition, stack.toMutableList()) }

        if (newTransition.expedite) {
            this.route.value = newTransition
        } else {
            operationChannel.send(ForwardOngoingOperation(newTransition))
        }

        mutex.unlock()
    }

    override suspend fun popTo(route: R, configuration: TransitionScope.() -> Unit): Boolean {
        if (!mutex.tryLock()) {
            Napier.w(tag = TAG, message = "Failed to acquire a mutex, popTo")
            // there is some operation in place
            return false
        }

        Napier.d(tag = TAG, message = "Popping to $route")

        val indexOfRoute = stack.indexOfLast { it.destination == route }

        if (indexOfRoute == -1) {
            return false
        }

        val removedTransitions = stack.subList(fromIndex = indexOfRoute + 1, toIndex = stack.size).toMutableList()
        stack.removeAll(removedTransitions)
        val newTransition = TransitionBuilder(stack.last()).apply(configuration).build()

        stack.replaceLast(newTransition)
        listeners.forEach { it.onPopped(removedTransitions, stack.toMutableList()) }

        if (newTransition.expedite) {
            this.route.value = newTransition
        } else {
            operationChannel.send(BackwardOngoingOperation(newTransition))
        }

        mutex.unlock()
        return true
    }

    override suspend fun goTo(route: R, configuration: TransitionScope.() -> Unit) {
        if (!mutex.tryLock()) {
            Napier.w(tag = TAG, message = "Failed to acquire a mutex, goTo operation")
            // there is some operation in place
            return
        }

        Napier.d(tag = TAG, message = "Going to $route")

        val transition = TransitionBuilder(route).apply(configuration).build()
        stack.add(transition)
        listeners.forEach { it.onPushed(transition, stack.toMutableList()) }
        operationChannel.send(ForwardOngoingOperation(transition))
        Napier.d(tag = TAG, message = "Unlocking mutex - goTo")
        mutex.unlock()
    }

    override suspend fun goToWithBackStack(
        route: R,
        backStack: List<Transition<R>>,
        configuration: TransitionScope.() -> Unit,
    ) {
        if (!mutex.tryLock()) {
            Napier.w(tag = TAG, message = "Failed to acquire a mutex, goToWithBackStack operation")
            // there is some operation in place
            return
        }

        Napier.d(tag = TAG, message = "goToWithBackStack, $route, size: ${backStack.size}")

        backStack.forEach { backStackTransition ->
            stack.add(backStackTransition)
            listeners.forEach { it.onPushed(backStackTransition, stack.toMutableList()) }
        }

        val newTransition = TransitionBuilder(route).apply(configuration).build()
        stack.add(newTransition)
        listeners.forEach { it.onPushed(newTransition, stack.toMutableList()) }

        if (newTransition.expedite) {
            this.route.value = newTransition
        } else {
            operationChannel.send(ForwardOngoingOperation(newTransition))
        }

        mutex.unlock()
    }

    override fun addListener(listener: IStackListener<R>) {
        this.listeners.add(listener)
    }

    override fun removeListener(listener: IStackListener<R>) {
        this.listeners.remove(listener)
    }

    override fun <T : Any> onArgumentRemoved(transition: Transition<R>, argument: Argument<T>) {
        val newTransition = transition.copy(
            arguments = transition.arguments.toMutableMap().apply {
                remove(argument.storageKey)
            },
        )

        if (mutex.tryLock()) {
            val indexOfTransition = stack.indexOf(transition)
            if (indexOfTransition != -1) {
                stack.removeAt(indexOfTransition)
                stack.add(indexOfTransition, newTransition)
            }
            mutex.unlock()
        }
    }

    private inner class ForwardOngoingOperation(override val transition: Transition<R>) : IOngoingOperation<R> {
        override val isBack: Boolean = false

        override suspend fun start() {
            route.value = transition
        }
    }

    private inner class BackwardOngoingOperation(override val transition: Transition<R>) : IOngoingOperation<R> {
        override val isBack: Boolean = true

        override suspend fun start() {
            route.value = transition
        }
    }

    companion object {
        private const val TAG = "ScreenRouter"
    }
}
