package com.finevotech.marketorderapigateway.common.actor

import com.finevotech.marketorderapigateway.common.RingBufferQueue
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class Scheduler {
    private val ringBufferQueue  = RingBufferQueue<ReadyContinuation<Any>>(1024)

    @Suppress("UNCHECKED_CAST")
    fun <T> scheduleContinuation(continuation: Continuation<T>,
                                 value: T) {
        ringBufferQueue.offer(ReadyContinuation(continuation, value) as ReadyContinuation<Any>)
    }

    fun run() {
        var next = ringBufferQueue.poll()
        while(next != null){
            next.resume()
            next = ringBufferQueue.poll()
        }
    }
}

class ReadyContinuation<T>(
    var continuation: Continuation<T>? = null,
    var value: T? = null
) {
    fun resume() = continuation!!.resume(value!!)
}