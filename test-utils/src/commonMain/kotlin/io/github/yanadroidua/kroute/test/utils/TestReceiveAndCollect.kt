package io.github.yanadroidua.kroute.test.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.fail
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Test utility class that subscribes to the chanel in a different coroutine and collects all the data that later could
 * be collected in a suspending manner.
 *
 * **Note!** Do not forget to close the job, otherwise test will run forever.
 *
 * @param flow flow that class should observe/collect
 * @param scope execution scope
 */
class TestReceiveAndCollect<T>(
    private val flow: Flow<T>,
    private val scope: CoroutineScope,
) {

    constructor(
        channel: ReceiveChannel<T>,
        scope: CoroutineScope,
    ) : this(
        flow = channel.consumeAsFlow(),
        scope = scope,
    )

    /**
     * Represents the currently running subscribing job
     */
    private var currentJob: Job? = null

    /**
     * Internal queue of collected data. Follows FIFO approach.
     */
    private val dataQueue = Channel<T>(
        capacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    /**
     * Subscribes to the provided [flow].
     */
    fun subscribe(): Job {
        currentJob?.cancel()
        return scope.launch {
            flow.collect { data ->
                // it's safe to use trySend since the capacity of a channel is unlimited
                dataQueue.trySend(data)
            }
        }.also { currentJob = it }
    }

    /**
     * Reads the latest value from the collected queue or fails with timeout.
     * @param timeout time within which read operation should be performed, otherwise receiver would get a
     * [TimeoutCancellationException]
     */
    @Throws(TimeoutCancellationException::class, CancellationException::class)
    suspend fun pop(timeout: Duration = DEFAULT_TIMEOUT): T = withTimeout(timeout) {
        dataQueue.receive()
    }

    suspend fun ensureNoItemsAndClose(timeout: Duration = DEFAULT_TIMEOUT) = try {
        val data = pop(timeout)
        fail(
            """
            Expected to timeout within $timeout with no elements, but received $data instead.
            """.trimIndent(),
        )
    } catch (_: TimeoutCancellationException) {
        // expected exception
        close()
    }

    fun close() {
        currentJob?.cancel()
    }

    companion object {
        val DEFAULT_TIMEOUT = 500.toDuration(DurationUnit.MILLISECONDS)
    }
}
