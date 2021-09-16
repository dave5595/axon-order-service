package com.finevotech.orderservicecqrs.order.command

import com.finevotech.orderservicecqrs.event.*
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeExecutionType
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderStatus
import com.finevotech.orderservicecqrs.order.enum.OrderAction
import com.finevotech.orderservicecqrs.order.enum.OrderSide
import com.finevotech.orderservicecqrs.order.enum.OrderType
import com.finevotech.orderservicecqrs.order.enum.OrderValidity
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalTime
import java.time.ZonedDateTime

sealed class OrderCommand

data class NewOrderCommand(
    @TargetAggregateIdentifier
    val orderId: String,
    val accountId: String,
    val securityId: String,
    val securitySubType: String,
    val dealerCode: String,
    val cdsNumber: String,
    val expiryDate: ZonedDateTime? = null,
    val action: OrderAction,
    val type: OrderType,
    val side: OrderSide,
    val validity: OrderValidity,
    val quantity: Int,
    val price: Double? = null,
    var created: LocalTime
) : OrderCommand()

data class CancelOrderClientCommand(
    @TargetAggregateIdentifier val orderId: String
) : OrderCommand()

data class ReviseOrderClientCommand(@TargetAggregateIdentifier val orderId: String, val price: Double, val quantity: Int) : OrderCommand()

data class QueueOrderCommand(
    @TargetAggregateIdentifier
    val orderId: String,
    val exchangeOrderId: String,
    val previousExchangeOrderId: String? = null,
    val exchangeReferenceId: String,
    val executionId: String,
    val cdsNumber: String,
    val price: Double,
    val quantity: Int,
    val securityId: String,
    val securitySubType: String,
    val fixId: String,
    val dealerCode: String,
    val orderSide: OrderSide,
    val brokerId: String,
    val executionType: ExchangeExecutionType,
    val orderStatus: ExchangeOrderStatus,
    val accountId: String,
    val text: String
) : OrderCommand() {
    fun toOrderQueuedEvent() = OrderQueuedEvent(
        orderId = orderId,
        exchangeOrderId = exchangeOrderId,
        previousExchangeOrderId = previousExchangeOrderId,
        exchangeReferenceId = exchangeReferenceId,
        executionId = executionId,
        cdsNumber = cdsNumber,
        price = price,
        quantity = quantity,
        securityId = securityId,
        securitySubType = securitySubType,
        fixId = fixId,
        dealerCode = dealerCode,
        brokerId = brokerId,
        executionType = executionType,
        orderStatus = orderStatus,
        text = text,
        accountId = accountId,
        orderSide = orderSide
    )
}

data class FillOrderCommand(
    @TargetAggregateIdentifier
    val orderId: String,
    val exchangeOrderId: String,
    val previousExchangeOrderId: String? = null,
    val exchangeReferenceId: String,
    val executionId: String,
    val cdsNumber: String,
    val price: Double,
    val quantity: Int,
    val securityId: String,
    val securitySubType: String,
    val averagePrice: Double? = null,
    val transactedTime: ZonedDateTime,
    val orderSide: OrderSide,
    val orderStatus: ExchangeOrderStatus,
    val executionType: ExchangeExecutionType,
    val totalMatchedQuantity: Int? = null,
    val totalUnmatchedQuantity: Int? = null,
    val text: String = "",
    val contraFirm: String,
    val contraTrader: String,
    val fixId: String,
    val brokerId: String,
    val accountId: String,
    val dealerCode: String
) : OrderCommand() {
    fun toOrderFilledEvent() = OrderFilledEvent(
        orderId,
        exchangeOrderId,
        previousExchangeOrderId,
        exchangeReferenceId,
        executionId,
        cdsNumber,
        price,
        quantity,
        securityId,
        securitySubType,
        averagePrice,
        transactedTime,
        orderSide,
        orderStatus,
        executionType,
        totalMatchedQuantity,
        totalUnmatchedQuantity,
        text,
        contraFirm,
        contraTrader,
        fixId,
        brokerId,
        accountId,
        dealerCode
    )
}

data class CancelOrderCommand(
    @TargetAggregateIdentifier
    val orderId: String,
    val exchangeOrderId: String,
    val previousExchangeOrderId: String? = null,
    val exchangeReferenceId: String,
    val executionId: String,
    val cdsNumber: String,
    val price: Double,
    val quantity: Int,
    val securityId: String,
    val securitySubType: String,
    val totalMatchedQuantity: Int? = null,
    val totalUnmatchedQuantity: Int? = null,
    val fixId: String,
    val dealerCode: String,
    val orderSide: OrderSide,
    val brokerId: String,
    val executionType: ExchangeExecutionType,
    val orderStatus: ExchangeOrderStatus,
    val accountId: String,
    val text: String
) : OrderCommand() {
    fun toOrderCancelledEvent() = OrderCancelledEvent(
        orderId = orderId,
        exchangeOrderId = exchangeOrderId,
        previousExchangeOrderId = previousExchangeOrderId,
        exchangeReferenceId = exchangeReferenceId,
        executionId = executionId,
        cdsNumber = cdsNumber,
        price = price,
        quantity = quantity,
        securityId = securityId,
        securitySubType = securitySubType,
        totalMatchedQuantity = totalMatchedQuantity,
        totalUnmatchedQuantity = totalUnmatchedQuantity,
        fixId = fixId,
        dealerCode = dealerCode,
        orderSide = orderSide,
        brokerId = brokerId,
        executionType = executionType,
        orderStatus = orderStatus,
        text = text,
        accountId = accountId
    )
}

data class ReviseOrderCommand(
    @TargetAggregateIdentifier
    val orderId: String,
    val exchangeOrderId: String,
    val previousExchangeOrderId: String? = null,
    val exchangeReferenceId: String,
    val executionId: String,
    val cdsNumber: String,
    val price: Double,
    val quantity: Int,
    val securityId: String,
    val securitySubType: String,
    val totalMatchedQuantity: Int? = null,
    val totalUnmatchedQuantity: Int? = null,
    val fixId: String,
    val dealerCode: String,
    val orderSide: OrderSide,
    val brokerId: String,
    val executionType: ExchangeExecutionType,
    val orderStatus: ExchangeOrderStatus,
    val text: String,
    val accountId: String
) : OrderCommand() {
    fun toOrderRevisedEvent() = OrderRevisedEvent(
        orderId = orderId,
        exchangeOrderId = exchangeOrderId,
        previousExchangeOrderId = previousExchangeOrderId,
        exchangeReferenceId = exchangeReferenceId,
        executionId = executionId,
        cdsNumber = cdsNumber,
        price = price,
        quantity = quantity,
        securityId = securityId,
        securitySubType = securitySubType,
        totalMatchedQuantity = totalMatchedQuantity,
        totalUnmatchedQuantity = totalUnmatchedQuantity,
        fixId = fixId,
        dealerCode = dealerCode,
        orderSide = orderSide,
        brokerId = brokerId,
        executionType = executionType,
        orderStatus = orderStatus,
        text = text,
        accountId = accountId
    )
}

data class ExpireOrderCommand(
    @TargetAggregateIdentifier
    val orderId: String,
    val exchangeOrderId: String,
    val previousExchangeOrderId: String? = null,
    val exchangeReferenceId: String,
    val executionId: String,
    val cdsNumber: String,
    val price: Double,
    val quantity: Int,
    val securityId: String,
    val securitySubType: String,
    val totalMatchedQuantity: Int? = null,
    val totalUnmatchedQuantity: Int? = null,
    val fixId: String,
    val dealerCode: String,
    val orderSide: OrderSide,
    val brokerId: String,
    val executionType: ExchangeExecutionType,
    val orderStatus: ExchangeOrderStatus,
    val text: String,
    val accountId: String
) : OrderCommand() {
    fun toOrderExpiredEvent() = OrderExpiredEvent(
        orderId = orderId,
        exchangeOrderId = exchangeOrderId,
        previousExchangeOrderId = previousExchangeOrderId,
        exchangeReferenceId = exchangeReferenceId,
        executionId = executionId,
        cdsNumber = cdsNumber,
        price = price,
        quantity = quantity,
        securityId = securityId,
        securitySubType = securitySubType,
        totalMatchedQuantity = totalMatchedQuantity,
        totalUnmatchedQuantity = totalUnmatchedQuantity,
        fixId = fixId,
        dealerCode = dealerCode,
        orderSide = orderSide,
        brokerId = brokerId,
        executionType = executionType,
        orderStatus = orderStatus,
        text = text,
        accountId = accountId
    )
}

data class RejectOrderCommand(
    @TargetAggregateIdentifier
    val orderId: String,
    val exchangeOrderId: String,
    val previousExchangeOrderId: String? = null,
    val exchangeReferenceId: String? = null,
    val executionId: String? = null,
    val cdsNumber: String,
    val price: Double,
    val quantity: Int,
    val securityId: String,
    val securitySubType: String,
    val totalMatchedQuantity: Int? = null,
    val totalUnmatchedQuantity: Int? = null,
    val fixId: String? = null,
    val dealerCode: String,
    val orderSide: OrderSide? = null,
    val brokerId: String? = null,
    val executionType: ExchangeExecutionType,
    val accountId: String,
    val orderStatus: ExchangeOrderStatus,
    val text: String? = null
) : OrderCommand() {
    fun toOrderRejectedEvent() = OrderRejectedEvent(
        orderId = orderId,
        exchangeOrderId = exchangeOrderId,
        previousExchangeOrderId= previousExchangeOrderId,
        exchangeReferenceId = exchangeReferenceId,
        executionId = executionId,
        cdsNumber = cdsNumber,
        price = price,
        quantity = quantity,
        securityId = securityId,
        securitySubType = securitySubType,
        totalMatchedQuantity = totalMatchedQuantity,
        totalUnmatchedQuantity = totalUnmatchedQuantity,
        fixId = fixId,
        dealerCode = dealerCode,
        orderSide = orderSide,
        brokerId = brokerId,
        executionType = executionType,
        orderStatus = orderStatus,
        text = text,
        accountId = accountId
    )
}

data class ConfirmOrderExecutionCommand(@TargetAggregateIdentifier val orderId: String): OrderCommand()
data class ConfirmOrderCommand(
    @TargetAggregateIdentifier
    private val orderId: String
) : OrderCommand()

data class ExecuteOrderCommand(@TargetAggregateIdentifier val orderId: String, val accountId: String) : OrderCommand()