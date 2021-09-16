package com.finevotech.orderservicecqrs.event

import com.yoda.mo.api.model.order.HasClientOrderId

data class OrderConfirmedEvent(val orderId: String, val clientOrderId: String, override val accountId: String) : OrderEvent
