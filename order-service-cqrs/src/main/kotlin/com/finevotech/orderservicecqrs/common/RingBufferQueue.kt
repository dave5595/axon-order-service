package com.finevotech.marketorderapigateway.common

import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class RingBufferQueue<T>(requestedCapacity: Int) : Queue<T> {
    val capacity: Int = validatePowerOfTwoCapacity(this, requestedCapacity)

    @Suppress("UNCHECKED_CAST")
    private val ring: Array<T?> = Array<Any?>(capacity) { null } as Array<T?>
    private val mask = capacity - 1
    private var read = 0
    private var write = 0
    override val size: Int
        get() = write - read
    val isFull: Boolean
        get() = size == capacity
    val remaining: Int
        get() = capacity - size

    override fun remove(): T {
        if (isEmpty()) {
            throw NoSuchElementException("remove called on empty RingBufferQueue")
        } else {
            val readLocation = read.and(mask)
            read += 1
            val result = ring[readLocation]
            ring[readLocation] = null
            return result!!
        }
    }

    override fun contains(element: T?): Boolean {
        val length = size
        var x = 0
        while (x < length) {
            if (ring[(read + x).and(mask)] == element) {
                return true
            }
            x += 1
        }
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        elements.forEach { element ->
            if (!contains(element)) {
                return false
            }
        }

        return true
    }

    override fun isEmpty(): Boolean = write == read

    override fun addAll(elements: Collection<T>): Boolean {
        if (remaining < elements.size) {
            return false
        }

        elements.forEachIndexed { index, element ->
            ring[(write + index).and(mask)] = element
        }

        write += elements.size
        return true
    }

    override fun clear() {
        while (size > 0) {
            ring[read.and(mask)] = null
            read += 1
        }
        write = 0
        read = 0
    }

    override fun offer(element: T?): Boolean {
        if (isFull) {
            return false
        }

        ring[write.and(mask)] = element
        write += 1
        return true
    }

    override fun element(): T? {
        if (isEmpty()) {
            throw NoSuchElementException("element called on empty RingBufferQueue")
        }

        return ring[read.and(mask)]
    }

    override fun add(element: T?): Boolean {
        if (isFull) {
            throw IllegalStateException("add called on full RingBufferQueue")
        }

        ring[write.and(mask)] = element
        write += 1
        return true
    }

    override fun peek(): T? {
        return if (isEmpty()) {
            null
        } else {
            ring[read.and(mask)]
        }
    }

    override fun poll(): T? {
        return if (isEmpty()) {
            null
        } else {
            val readLocation = read.and(mask)
            read += 1
            val result = ring[readLocation]
            ring[readLocation] = null
            return result
        }
    }

    override fun iterator(): MutableIterator<T> {
        throw UnsupportedOperationException()
    }

    override fun remove(element: T?): Boolean {
        throw UnsupportedOperationException()
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        throw UnsupportedOperationException()
    }
}

internal inline fun <reified T : Any> validatePowerOfTwoCapacity(
    caller: T,
    value: Int
): Int {
    return if (isPowerOfTwo(value)) {
        value
    } else {
        println(
            "${caller.javaClass.simpleName} capacity should be a power of two, but ($value) requested. Using the next available: ${
                roundToNextPowerOfTwo(
                    value
                )
            }."
        )
        roundToNextPowerOfTwo(value)
    }
}

private const val MAX_POW2 = 1 shl 30

fun roundToNextPowerOfTwo(value: Int): Int {
    if (value > MAX_POW2) {
        throw IllegalArgumentException("There is no larger power of 2 int for value:$value since it exceeds 2^31.")
    }
    if (value < 0) {
        throw IllegalArgumentException("Given value:$value. Expecting value >= 0.")
    }
    return 1 shl 32 - Integer.numberOfLeadingZeros(value - 1)
}

fun isPowerOfTwo(value: Int): Boolean = value == roundToNextPowerOfTwo(value)
