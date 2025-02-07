package io.github.yanadroidua.kroute.viewmodel

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Represents a ViewModel that has a CoroutineScope that is tied to the [IViewModel] lifecycle.
 * @param dispatcher the default [CoroutineDispatcher] that VM will be using for its operations.
 */
abstract class CoroutineViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : IViewModel {

    protected var viewModelScope: CoroutineScope? = null
        private set

    override var isActive: Boolean = false
        protected set

    override var isAboutToBeDestroyed: Boolean = false
        protected set

    override var isPaused: Boolean = false
        protected set

    override fun onStart() {
        isPaused = false
        startJob()
        isActive = true
    }

    override fun onDestroy() {
        isActive = false
        isAboutToBeDestroyed = true
        terminateJob()
        isPaused = false
    }

    override fun onPause() {
        isActive = false
        terminateJob()
        isPaused = true
    }

    private fun <T> doInScope(action: CoroutineScope.() -> T): T? = viewModelScope
        ?.takeIf { it.isActive }
        ?.let { scope -> action(scope) }
        ?: run {
            Napier.w {
                """
                        Cannot run action because VM's coroutine scope has not been created.
                        isActive: ${viewModelScope?.isActive ?: "NULL"}
                """.trimIndent()
            }
            null
        }

    protected fun launch(block: suspend () -> Unit) {
        launchJob(block = block)
    }

    protected fun launchJob(block: suspend () -> Unit): Job? = doInScope { launch { block() } }

    protected fun <T> async(block: suspend () -> T): Deferred<T>? = doInScope { this.async { block() } }

    private fun startJob(): CoroutineScope {
        Napier.d { "Creating new VM's coroutine scope" }
        viewModelScope?.cancel()
        return CoroutineScope(context = SupervisorJob() + dispatcher).also {
            viewModelScope = it
        }
    }

    private fun terminateJob() {
        Napier.d { "Terminating VM's coroutine scope" }
        viewModelScope?.cancel()
        viewModelScope = null
    }
}
