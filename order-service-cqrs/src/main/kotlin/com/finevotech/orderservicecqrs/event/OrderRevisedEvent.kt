package com.finevotech.orderservicecqrs.event

import com.finevotech.orderservicecqrs.exchange.enum.ExchangeExecutionType
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderStatus
import com.finevotech.orderservicecqrs.order.enum.OrderSide

data class OrderRevisedEvent(
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
    override val accountId: String,
    val text: String
) : OrderEvent


