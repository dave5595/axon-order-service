package com.finevotech.orderservicecqrs.event

import com.finevotech.orderservicecqrs.exchange.enum.ExchangeExecutionType
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderStatus
import com.finevotech.orderservicecqrs.order.enum.OrderSide
import java.time.ZonedDateTime

data class OrderFilledEvent(
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
    override val accountId: String,
    val dealerCode: String
) : OrderEvent


