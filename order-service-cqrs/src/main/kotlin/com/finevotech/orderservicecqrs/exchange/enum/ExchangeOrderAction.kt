package com.finevotech.orderservicecqrs.exchange.enum

import com.fasterxml.jackson.annotation.JsonValue
import com.finevotech.orderservicecqrs.order.enum.OrderAction

enum class ExchangeOrderAction(@get:JsonValue val action: String)  {
    New("new"),
    Replace("replace"),
    Cancel("cancel");

    fun toOmsOrderAction() = when (this) {
        New -> OrderAction.New
        Replace -> OrderAction.Replace
        Cancel -> OrderAction.Cancel
    }
}