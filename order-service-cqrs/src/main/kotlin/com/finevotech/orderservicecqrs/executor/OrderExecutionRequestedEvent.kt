package com.finevotech.orderservicecqrs.executor

import com.finevotech.orderservicecqrs.order.enum.OrderAction
import com.finevotech.orderservicecqrs.order.enum.OrderSide
import com.finevotech.orderservicecqrs.order.enum.OrderType
import com.finevotech.orderservicecqrs.order.enum.OrderValidity
import java.time.ZonedDateTime


data class OrderExecutionRequestedEvent(
    val exchangeOrderId: String,
    val previousExchangeOrderId: String? = null,
    val clientOrderId: String,
    val accountId: String,
    val cdsNumber: String,
    val dealerCode: String,
    val securityId: String,
    val securitySubType: String,
    val expiryDate: ZonedDateTime? = null,
    val orderAction: OrderAction,
    val orderType: OrderType,
    val orderSide: OrderSide,
    val orderValidity: OrderValidity,
    val quantity: Int,
    val price: Double
)