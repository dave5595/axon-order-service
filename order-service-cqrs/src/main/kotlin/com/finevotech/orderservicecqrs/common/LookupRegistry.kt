package com.finevotech.marketorderapigateway.common

import java.util.concurrent.ConcurrentHashMap


open class LookupRegistry<K, V> {
    private val lookup = ConcurrentHashMap<K, V>()

    fun contains(key: K) = lookup.contains(key)

    fun count() = lookup.count()

    operator fun get(key: K) = lookup[key]

    fun remove(key: K) = lookup.remove(key!!)

    operator fun set(key: K, value: V) {
        lookup[key] = value
    }

}