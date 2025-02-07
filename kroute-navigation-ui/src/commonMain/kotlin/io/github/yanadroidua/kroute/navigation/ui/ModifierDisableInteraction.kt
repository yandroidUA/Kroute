package io.github.yanadroidua.kroute.navigation.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.unit.IntSize

private class AllowUserInputNode(var allowInput: Boolean) :
    Modifier.Node(),
    PointerInputModifierNode {
    override fun onCancelPointerInput() = Unit

    override fun onPointerEvent(pointerEvent: PointerEvent, pass: PointerEventPass, bounds: IntSize) {
        if (allowInput) return
        pointerEvent.changes.forEach { it.consume() }
    }
}

@Immutable
private data class AllowUserInputModifier(val allowInput: Boolean) : ModifierNodeElement<AllowUserInputNode>() {
    override fun create(): AllowUserInputNode = AllowUserInputNode(allowInput = allowInput)

    override fun update(node: AllowUserInputNode) {
        node.allowInput = allowInput
    }
}

/**
 * A modifier that steals all user's input events. It's useful if you want to disable user interaction while router is
 * performing a transition.
 *
 * @param allowInput whether the user's input is allowed to pass down to the composables
 */
fun Modifier.allowUserInput(allowInput: Boolean) = AllowUserInputModifier(allowInput = allowInput) then this
