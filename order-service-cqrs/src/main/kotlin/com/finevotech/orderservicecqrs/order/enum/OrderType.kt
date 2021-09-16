package com.finevotech.orderservicecqrs.order.enum

import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderType

enum class OrderType {
    Market,
    Limit,
    StopLoss,
    StopLimit;

    fun convertToExchangeOrderType(): ExchangeOrderType {
        return when (this) {
            Limit -> ExchangeOrderType.Limit
            Market -> ExchangeOrderType.Market
            StopLoss -> ExchangeOrderType.StopOrStopLoss
            StopLimit -> ExchangeOrderType.StopLimit
        }
    }
}

