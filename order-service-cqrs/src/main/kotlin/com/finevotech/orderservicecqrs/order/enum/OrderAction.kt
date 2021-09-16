package com.finevotech.orderservicecqrs.order.enum

import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderAction

enum class OrderAction {
    New,
    Replace,
    Cancel;

    fun toExchangeOrderAction(): ExchangeOrderAction {
        return when (this) {
            New -> ExchangeOrderAction.New
            Replace -> ExchangeOrderAction.Replace
            Cancel -> ExchangeOrderAction.Cancel
        }
    }
}