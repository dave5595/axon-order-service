package com.finevotech.orderservicecqrs.event

data class OrderExecutedEvent(val orderId: String, override val accountId: String) : OrderEvent
