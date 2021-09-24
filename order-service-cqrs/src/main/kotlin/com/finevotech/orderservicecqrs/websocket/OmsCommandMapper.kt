package com.finevotech.orderservicecqrs.websocket

import com.finevotech.marketorderapigateway.common.JsonMapper
import com.finevotech.marketorderapigateway.common.Logger
import com.finevotech.orderservicecqrs.order.command.NewOrderCommand
import com.finevotech.orderservicecqrs.order.command.OrderCommand
import org.springframework.web.reactive.socket.WebSocketMessage
import reactor.core.publisher.Mono
import java.time.LocalTime

object OmsCommandMapper {
    val log by Logger()
    fun toCommandAsync(message: WebSocketMessage): Mono<OrderCommand> {
        return Mono.fromCallable {
           JsonMapper.readValue(message.payloadAsText, NewOrderCommand::class.java).apply {
               created = LocalTime.now()
           }
        }
    }

    fun toCommand(message: WebSocketMessage): OrderCommand {
        lateinit var command: NewOrderCommand
        val payload = message.payloadAsText
        command = JsonMapper.readValue(payload, NewOrderCommand::class.java)
        command.created = LocalTime.now()
        return command
    }
}
