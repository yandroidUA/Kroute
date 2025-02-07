package io.github.yanadroidua.kroute.navigation.router

import io.github.yanadroidua.kroute.navigation.transition.Transition

/**
 * A router stack listener.
 */
interface IStackListener<R : Any> {
    /**
     * Invokes every time a new transition get pushed to the router's stack.
     *
     * @param transition new transition that has been pushed
     * @param stack current stack, **including new transition**
     */
    fun onPushed(transition: Transition<R>, stack: List<Transition<R>>) {}

    /**
     * Invokes every time a transitions got popped out of the router's stack.
     *
     * @param popped a list of transitions that have been popped out of the stack.
     * @param stack current stack, **excluding popped transitions**
     */
    fun onPopped(popped: List<Transition<R>>, stack: List<Transition<R>>) {}
}
