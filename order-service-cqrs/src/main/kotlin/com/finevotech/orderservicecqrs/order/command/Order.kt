@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package com.finevotech.orderservicecqrs.order.command

import com.finevotech.marketorderapigateway.common.Logger
import com.finevotech.orderservicecqrs.event.*
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderStatus
import com.finevotech.orderservicecqrs.executor.OrderExecutionRequestedEvent
import com.finevotech.orderservicecqrs.order.enum.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.util.Assert
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit.MILLIS
import java.util.*


//RequestNewOrderCommand -> OrderCreatedEvent -> ExecuteOrderCommand -> OrderExecutionRequestedEvent -> ConfirmOrderExecutionCommand -> OrderExecutedEvent
//                                                                                                    -> RejectOrderCommand -> OrderRejected
//todo implement exchange status feature/orderHolding logic
@Aggregate
class Order {
    @AggregateIdentifier(routingKey = "orderId")
    private lateinit var clientOrderId: String
    private var orderId: String = UUID.randomUUID().toString()
    private val exchangeOrderId: String = UUID.randomUUID().toString()
    private val previousExchangeOrderId: String? = null
    private lateinit var accountId: String
    private lateinit var cdsNumber: String
    private lateinit var dealerCode: String
    private lateinit var securityId: String
    private lateinit var securitySubType: String
    private var expiryDate: ZonedDateTime? = null
    private lateinit var action: OrderAction
    private lateinit var type: OrderType
    private lateinit var side: OrderSide
    private lateinit var validity: OrderValidity
    private var quantity: Int = 0
    private var price: Double? = null
    private lateinit var state: OrderState
    private lateinit var createdAtSource: LocalTime

    private val log by Logger()

    constructor() {}

    @CommandHandler
    constructor(command: NewOrderCommand) {
        log.info("received new order command")
        val (orderId, accountId, securityId, securitySubType, dealerCode, cdsNumber, expiryDate, action, type, side, validity, quantity, price, created) = command
        apply(
            OrderCreatedEvent(
                clientOrderId = orderId,
                accountId = accountId,
                securityId = securityId,
                securitySubType = securitySubType,
                dealerCode = dealerCode,
                cdsNumber = cdsNumber,
                expiryDate = expiryDate,
                action = action,
                type = type,
                side = side,
                validity = validity,
                quantity = quantity,
                price = price,
                created = created
            )
        )
    }

    @CommandHandler
    fun revise(command: ReviseOrderClientCommand) {
        Assert.isTrue(!isRevisable(), "Order can not be revised, order state = $state")
        Assert.isTrue(
            isReviseRedundant(newPrice = command.price, newQuantity = command.quantity),
            "Revise action is redundant. No change in quantity and price"
        )
        apply(OrderRevisingEvent(orderId = command.orderId, accountId = accountId))
    }

    @CommandHandler
    fun cancel(command: CancelOrderClientCommand) {
        Assert.isTrue(!isCancellable(), "Order can not be updated as cancelled, order state = $state")
        apply(OrderCancellingEvent(orderId = command.orderId, accountId = accountId))
    }

    @CommandHandler
    fun revise(command: ReviseOrderCommand) {
        Assert.isTrue(ableToUpdateAsRevised(), "Order can not be updated as revised, order state = $state")
        apply(command.toOrderRevisedEvent())
    }

    @CommandHandler
    fun cancel(command: CancelOrderCommand) {
        Assert.isTrue(ableToUpdateAsCancelled(), "Order can not be updated as cancelled, order state = $state")
        apply(command.toOrderCancelledEvent())
    }

    @CommandHandler
    fun fill(command: FillOrderCommand) {
        apply(command.toOrderFilledEvent())
    }

    @CommandHandler
    fun expire(command: ExpireOrderCommand) {
        Assert.isTrue(ableToUpdateAsExpired(), "Order can not be updated as expired, order state = $state")
        apply(command.toOrderExpiredEvent())
    }

    @CommandHandler
    fun reject(command: RejectOrderCommand) {
        Assert.isTrue(ableToUpdateAsRejected(), "Order can not be updated as rejected, order state = $state")
        apply(command.toOrderRejectedEvent())
    }

    @CommandHandler
    fun handle(command: ConfirmOrderExecutionCommand) {
        apply(OrderExecutedEvent(orderId = command.orderId, accountId = accountId))
    }

    @CommandHandler
    fun execute(command: ExecuteOrderCommand) {
        log.info("execute order event received")
        apply(
            OrderExecutionRequestedEvent(
                clientOrderId = clientOrderId,
                exchangeOrderId = exchangeOrderId,
                previousExchangeOrderId = previousExchangeOrderId,
                accountId = accountId,
                cdsNumber = cdsNumber,
                dealerCode = dealerCode,
                securityId = securityId,
                securitySubType = securitySubType,
                quantity = quantity,
                price = price!!,
                orderSide = side,
                orderValidity = validity,
                orderType = type,
                expiryDate = expiryDate,
                orderAction = action
            )
        )
    }

    @EventSourcingHandler
    fun on(event: OrderCreatedEvent) {
        log.info("order created event received")
        clientOrderId = event.clientOrderId
        accountId = event.accountId
        cdsNumber = event.cdsNumber
        dealerCode = event.dealerCode
        securityId = event.securityId
        securitySubType = event.securitySubType
        quantity = event.quantity
        price = event.price!!
        side = event.side
        validity = event.validity
        type = event.type
        expiryDate = event.expiryDate
        action = event.action
        createdAtSource = event.created
        setState(Confirmed)
    }

    @EventSourcingHandler
    fun on(event: OrderQueuedEvent) {
        setState(Queued)
    }

    @EventSourcingHandler
    fun on(event: OrderCancellingEvent) {
        setState(Cancelling)
    }

    @EventSourcingHandler
    fun on(event: OrderRevisingEvent) {
        setState(Revising)
    }

    @EventSourcingHandler
    fun on(event: OrderFilledEvent) {
        if (event.orderStatus == ExchangeOrderStatus.Filled) {
            setState(Matched)
        } else {
            setState(PartialMatched)
        }
    }

    @EventSourcingHandler
    fun on(event: OrderExpiredEvent) {
        setState(Expired)
    }

    @EventSourcingHandler
    fun on(event: OrderExecutedEvent) {
        val now = LocalTime.now()
        log.info(
            "elapse to execute: ${
                createdAtSource.until(
                    now,
                    MILLIS
                )
            }ms. created at source: $createdAtSource, now: $now"
        )
        setState(Executed)
    }

    @EventSourcingHandler
    fun on(event: OrderCancelledEvent) {
        setState(Cancelled)
    }

    @EventSourcingHandler
    fun on(event: OrderRevisedEvent) {
        quantity = event.quantity
        price = event.price
        setState(Queued)
    }

    @EventSourcingHandler
    fun on(event: OrderRejectedEvent) {
        setState(Rejected)
    }

    private fun ableToUpdateAsRejected() = state == Confirmed || state == Executed || state == Queued
    private fun ableToUpdateAsCancelled() = state == Cancelling
    private fun ableToUpdateAsRevised() = state == Revising
    private fun ableToUpdateAsQueued() = state == Executed
    private fun ableToUpdateAsExpired(): Boolean = TODO()
    private fun isReviseRedundant(newPrice: Double, newQuantity: Int) = newPrice == price && newQuantity == quantity
    private fun isRevisable() = state == Queued
    private fun isCancellable() = state == Queued || state == PartialMatched
    private fun setState(state: OrderState) {
        this.state = state
    }
}