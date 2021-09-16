package com.finevotech.orderservicecqrs.event

sealed interface Event

sealed interface OrderEvent: Event {
    val accountId: String
}