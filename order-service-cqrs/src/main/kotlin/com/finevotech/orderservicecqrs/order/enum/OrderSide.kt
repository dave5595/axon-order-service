package com.finevotech.orderservicecqrs.order.enum

import com.fasterxml.jackson.annotation.JsonValue
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderSide

enum class OrderSide {
    Bid, Ask;
}

fun OrderSide.convertToExchangeOrderSide(): ExchangeOrderSide {
    return when (this) {
        OrderSide.Bid -> ExchangeOrderSide.Buy
        OrderSide.Ask -> ExchangeOrderSide.Sell
    }
}