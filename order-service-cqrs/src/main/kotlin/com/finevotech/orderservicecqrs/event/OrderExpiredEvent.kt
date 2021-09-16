package com.finevotech.orderservicecqrs.event

import com.finevotech.orderservicecqrs.exchange.enum.ExchangeExecutionType
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderStatus
import com.finevotech.orderservicecqrs.order.enum.OrderSide

data class OrderExpiredEvent(
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
    val orderStatus: ExchangeOrderStatus,
    override val accountId: String,
    val text: String? = null
) : OrderEvent
