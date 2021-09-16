package com.finevotech.orderservicecqrs.exchange.enum

import com.fasterxml.jackson.annotation.JsonValue
import com.finevotech.orderservicecqrs.order.enum.OrderSide

enum class ExchangeOrderSide(@get:JsonValue val status: String) {
    Buy("1"),
    Sell("2");
}

fun ExchangeOrderSide.toOrderSide(): OrderSide {
    return when (this){
        ExchangeOrderSide.Buy -> OrderSide.Bid
        ExchangeOrderSide.Sell -> OrderSide.Ask
    }
}