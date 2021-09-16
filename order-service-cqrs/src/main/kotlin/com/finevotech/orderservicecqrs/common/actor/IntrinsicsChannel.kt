package com.finevotech.marketorderapigateway.common.actor

import com.finevotech.marketorderapigateway.common.RingBufferQueue
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED

interface SuspendingQueue<T> {
    suspend fun take(): T
    suspend fun add(value: T): Boolean
}

class IntrinsicsSuspendingQueue<T>(val scheduler: Scheduler,
                                   capacity: Int) : SuspendingQueue<T> {
    private val buffer: Queue<T> = RingBufferQueue(capacity)
    private var fullWaiter: Continuation<Unit>? = null
    private var emptyWaiter: Continuation<T>? = null

    private fun offer(value: T): Boolean {
        val fullWaiter = this.emptyWaiter
        if (fullWaiter != null) {
            this.emptyWaiter = null
            scheduler.scheduleContinuation(fullWaiter, value)
            return true
        }
        return buffer.offer(value)
    }

    override suspend fun add(value: T): Boolean {
        if (offer(value)) {
            return true
        }
        return suspendAdd(value)
    }

    private tailrec suspend fun suspendAdd(value: T): Boolean {
        suspendCoroutineUninterceptedOrReturn<Unit> { continuation ->
            fullWaiter = continuation
            COROUTINE_SUSPENDED
        }
        if (buffer.offer(value)) {
            return true
        }
        return suspendAdd(value)
    }

    private suspend fun suspendTake(): T {
        return suspendCoroutineUninterceptedOrReturn { continuation ->
            emptyWaiter = continuation
            COROUTINE_SUSPENDED
        }
    }

    private fun poll(): T? {
        val result = buffer.poll()
        if (result != null) {
            val fullWaiter = this.fullWaiter
            if (fullWaiter != null) {
                this.fullWaiter = null
                scheduler.scheduleContinuation(fullWaiter, Unit)
            }
        }
        return result
    }

    override suspend fun take(): T {
        val result = poll()
        if (result != null) {
            return result
        }
        return suspendTake()
    }
}