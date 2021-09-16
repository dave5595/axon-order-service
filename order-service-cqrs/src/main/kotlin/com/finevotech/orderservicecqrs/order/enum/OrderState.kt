package com.finevotech.orderservicecqrs.order.enum

sealed class OrderState
object Confirmed: OrderState()
object Executed: OrderState()
object Queued: OrderState()
object Cancelled: OrderState()
object Revising: OrderState()
object Matched: OrderState()
object PartialMatched: OrderState()
object Cancelling: OrderState()
object Rejected: OrderState()
object Expired: OrderState()