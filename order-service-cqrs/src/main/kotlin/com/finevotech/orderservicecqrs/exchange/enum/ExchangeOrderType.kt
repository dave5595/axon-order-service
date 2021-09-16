package com.finevotech.orderservicecqrs.exchange.enum

import com.fasterxml.jackson.annotation.JsonValue

enum class ExchangeOrderType(@get:JsonValue val status: String) {
    Market("1"),
    Limit("2"),
    StopOrStopLoss("3"),
    StopLimit("4"),
    MarketOnClose("5"),
    WithOrWithout("6"),
    LimitOrBetter("7"),
    LimitWithOrWithout("8"),
    OnBasis("9")
}