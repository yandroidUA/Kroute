package io.github.yanadroidua.kroute.lifecycle.viewmodel.utils

import io.github.yanadroidua.kroute.viewmodel.CoroutineViewModel

internal class TestViewModel : CoroutineViewModel() {

    override fun hashCode(): Int = this::class.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        return true
    }
}
