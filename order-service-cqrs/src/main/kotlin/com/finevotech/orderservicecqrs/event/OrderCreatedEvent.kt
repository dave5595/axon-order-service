package com.finevotech.orderservicecqrs.event

import com.finevotech.orderservicecqrs.order.enum.OrderAction
import com.finevotech.orderservicecqrs.order.enum.OrderSide
import com.finevotech.orderservicecqrs.order.enum.OrderType
import com.finevotech.orderservicecqrs.order.enum.OrderValidity
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.*

data class OrderCreatedEvent(
    val clientOrderId: String,
    val orderId: String = UUID.randomUUID().toString(),
    override val accountId: String,
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
    val created: LocalTime
) : OrderEvent