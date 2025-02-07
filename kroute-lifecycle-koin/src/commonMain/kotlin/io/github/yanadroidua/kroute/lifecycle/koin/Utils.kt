package io.github.yanadroidua.kroute.lifecycle.koin

import org.koin.core.scope.ScopeID

/**
 * A utility function that uses class name as a scope identifier.
 */
inline fun <reified S : Any> scopeId(): ScopeID = requireNotNull(S::class.simpleName) {
    "[Kroute] Failed to get a class name to use it as a scope identifier"
}
