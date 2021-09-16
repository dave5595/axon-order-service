package com.finevotech.marketorderapigateway.common.coroutine

import kotlinx.coroutines.AbstractCoroutine
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

@Suppress("OverridingDeprecatedMember")
@InternalCoroutinesApi
open class ChannelCoroutine<E>(
    parentContext: CoroutineContext,
    private val _channel: Channel<E>,
    active: Boolean
) : AbstractCoroutine<Unit>(parentContext, true, active), Channel<E> by _channel {
    val channel: Channel<E> get() = this

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    final override fun cancel(cause: Throwable?): Boolean {
        cancelInternal(CancellationException(cause?.localizedMessage ?: cancellationExceptionMessage(), cause))
        return true
    }

    final override fun cancel(cause: CancellationException?) {
        if (isCancelled) return // Do not create an exception if the coroutine (-> the channel) is already cancelled
        cancelInternal(CancellationException(cause?.localizedMessage ?: cancellationExceptionMessage(), cause))
    }

    override fun cancel() {
        cancelInternal(CancellationException(cancellationExceptionMessage()))
    }

    override fun cancelInternal(cause: Throwable) {
        val exception = cause.toCancellationException()
        _channel.cancel(exception) // cancel the channel
        cancelCoroutine(exception) // cancel the job
    }

    companion object {
        protected fun cancellationExceptionMessage(): String = "Job was cancelled"

    }
}
