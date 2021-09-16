package com.finevotech.orderservicecqrs.externallisteners

import com.finevotech.orderservicecqrs.event.OrderEvent
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.DisposableBean
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Component
class OrderEventsSink : DisposableBean {
    private val sink: Sinks.Many<OrderEvent> = Sinks.many()
        .multicast()
        .onBackpressureBuffer(DEFAULT_BUFFER_SIZE)

    @ExceptionHandler
    fun handle(exception: Exception) {
        sink.tryEmitError(exception)
    }

    fun subscribe(): Flux<OrderEvent> {
        return sink.asFlux()
    }

    @EventHandler
    fun handle(event: OrderEvent) {
        sink.tryEmitNext(event)
    }

    override fun destroy() {
        sink.tryEmitComplete()
    }

}

