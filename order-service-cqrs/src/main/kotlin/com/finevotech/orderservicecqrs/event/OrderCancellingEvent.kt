package com.finevotech.orderservicecqrs.event

data class OrderCancellingEvent(val orderId: String, override val accountId: String) : OrderEvent
