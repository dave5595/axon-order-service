package com.finevotech.orderservicecqrs.order

import com.finevotech.orderservicecqrs.order.command.NewOrderCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/order")
class OrderController(private val commandGateway: CommandGateway) {

    @PostMapping
    fun placeOrder(@RequestBody command: NewOrderCommand): Any{
        return commandGateway.send<Any>(command)
    }
}