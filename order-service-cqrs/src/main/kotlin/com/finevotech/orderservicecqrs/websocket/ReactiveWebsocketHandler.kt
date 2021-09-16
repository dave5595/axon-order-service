package com.finevotech.orderservicecqrs.websocket

import com.finevotech.marketorderapigateway.common.JsonMapper
import com.finevotech.orderservicecqrs.event.OrderEvent
import com.finevotech.orderservicecqrs.externallisteners.OrderEventsSink
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.PooledDataBuffer
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.function.Predicate
import kotlin.math.log

@Component
class ReactiveWebsocketHandler(
    private val websocketAccountSessionManager: WebsocketAccountSessionManager,
    private val orderEventsSink: OrderEventsSink,
    private val commandGateway: CommandGateway
) : WebSocketHandler, CorsConfigurationSource {

    override fun handle(session: WebSocketSession): Mono<Void> {
        val accountId = getAccountId()
        //transforms input to a command and publishes them thru the gateway
        val input = session
            .receive()
            .flatMap(OmsCommandMapper::toCommandAsync, Schedulers.DEFAULT_POOL_SIZE)
            .subscribeOn(Schedulers.boundedElastic())
            .log()
            .doOnNext { commandGateway.send<Any>(it) }
            .then()

        //todo have a mainSink that listens to all event types and push related to account sessions
        //subscribes to account events and publishes to session 
        val source = orderEventsSink.subscribe()
            .filter(byAccountId(accountId))
            .map(JsonMapper::writeValueAsString)
            .map(session::textMessage)

        val output = session.send(source)

        return Mono.`when`(input, output)
    }

    private fun getAccountId(): String {
        return "account001"
    }

    private fun byAccountId(accountId: String) = Predicate<OrderEvent> { it.accountId == accountId }

    override fun getCorsConfiguration(exchange: ServerWebExchange): CorsConfiguration {
        val configuration = CorsConfiguration()
//        configuration.allowCredentials = true
        configuration.allowedOrigins = arrayListOf("*")
        configuration.allowedHeaders = arrayListOf("*")
        configuration.allowedMethods = arrayListOf("*")
        return configuration
    }
}
