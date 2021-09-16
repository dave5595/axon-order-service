package com.finevotech.orderservicecqrs.event

data class OrderRevisingEvent(val orderId: String, override val accountId: String) : OrderEvent
