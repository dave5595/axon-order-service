package com.finevotech.orderservicecqrs.order.command

import com.finevotech.marketorderapigateway.common.Logger
import com.finevotech.orderservicecqrs.event.OrderConfirmedEvent
import com.finevotech.orderservicecqrs.event.OrderCreatedEvent
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeExecutionType
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderStatus
import com.finevotech.orderservicecqrs.executor.OrderExecutionRequestedEvent
import com.finevotech.orderservicecqrs.executor.RabbitMarketOrderExecutor
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import java.time.LocalTime


//Accepted -> Executed -> Queued//Rejected
@Profile("rabbit-executor")
@Saga(sagaStore = "sagaStore")
class OrderManagerSaga {
    @Autowired
    private lateinit var commandGateway: CommandGateway

    @Autowired
    private lateinit var executor: RabbitMarketOrderExecutor
    private val log by Logger()

    @StartSaga
    @SagaEventHandler(associationProperty = "clientOrderId")
    fun on(event: OrderCreatedEvent) {
        log.info("saga received order created event")
        commandGateway.send<Any>(ExecuteOrderCommand(orderId = event.clientOrderId, accountId = event.accountId))
    }

    @SagaEventHandler(associationProperty = "clientOrderId")
    fun on(event: OrderExecutionRequestedEvent) {
        log.info("saga received order execution requested event")
        try {
//            executor.execute(event)
            commandGateway.send<Any>(ConfirmOrderExecutionCommand(orderId = event.clientOrderId))
        } catch (e: Exception) {
            log.error("failed to send order: $e")
            commandGateway.send<Any>(
                RejectOrderCommand(
                    orderId = event.clientOrderId,
                    exchangeOrderId = event.exchangeOrderId,
                    previousExchangeOrderId = event.previousExchangeOrderId,
                    cdsNumber = event.cdsNumber,
                    price = event.price,
                    quantity = event.quantity,
                    securityId = event.securityId,
                    securitySubType = event.securitySubType,
                    dealerCode = event.dealerCode,
                    orderSide = event.orderSide,
                    executionType = ExchangeExecutionType.Rejected,
                    orderStatus = ExchangeOrderStatus.Rejected,
                    accountId = event.accountId
                )
            )
        }
    }
}


