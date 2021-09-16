package com.finevotech.orderservicecqrs.websocket

import com.finevotech.marketorderapigateway.common.JsonMapper
import com.finevotech.orderservicecqrs.event.OrderEvent
import com.finevotech.orderservicecqrs.externallisteners.OrderEventsSink
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.server.WebSocketService
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.SignalType
import reactor.netty.http.server.WebsocketServerSpec
import java.util.function.Predicate


@Configuration
class WebsocketConfiguration(private val webSocketHandler: WebSocketHandler) {

    @Bean
    fun websocketHandlerAdapter() = WebSocketHandlerAdapter(handshakeWebSocketService())

    @Bean
    fun handshakeWebSocketService(): WebSocketService {
        return HandshakeWebSocketService(
            ReactorNettyRequestUpgradeStrategy(
                WebsocketServerSpec
                    .builder()
                    .handlePing(true)
            )
        )
    }

    @Bean
    fun websocketHandlerMapping(): HandlerMapping {
        val map = hashMapOf(
            "/api-ws" to webSocketHandler
        )
        val handlerMapping = SimpleUrlHandlerMapping()
        handlerMapping.order = 1
        handlerMapping.urlMap = map
        return handlerMapping
    }
}