package com.finevotech.marketorderapigateway.common.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ActorScope
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.produce
import kotlin.coroutines.CoroutineContext

@ObsoleteCoroutinesApi
abstract class AbstractActor<T>(scope: ActorScope<T>) : ActorScope<T> by scope {
    abstract suspend fun onReceive(msg: T)
}

@ObsoleteCoroutinesApi
fun <T> CoroutineScope.actorOf(
    context: CoroutineContext,
    init: (ActorScope<T>) -> AbstractActor<T>
) = actor<T>(context) {
    val instance = init(this)
    for (msg in channel) instance.onReceive(msg)
}

@ExperimentalCoroutinesApi
abstract class AbstractProducer<T>(scope: ProducerScope<T>) : ProducerScope<T> by scope {
    abstract suspend fun emit(msg: T)
}

@ExperimentalCoroutinesApi
fun <T> CoroutineScope.producerOf(
    context: CoroutineContext,
    init: (ProducerScope<T>) -> ProducerScope<T>
) = produce<T>() {
    val instance = init(this)


}