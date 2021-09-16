package com.finevotech.orderservicecqrs.exchange.enum

import com.fasterxml.jackson.annotation.JsonValue
import com.yoda.mo.api.model.enums.ExecutionType

enum class ExchangeExecutionType(@get:JsonValue val type: String) {
    New("0"),
    DoneForDay("3"),
    Cancelled("4"),
    Replaced("5"),
    PendingCancel("6"),
    Stopped("7"),
    Rejected("8"),
    Suspended("9"),
    PendingNew("A"),
    Calculated("B"),
    Expired("C"),
    Restated("D"),
    PendingReplace("E"),
    Trade("F"),
    TradeCorrect("G"),
    TradeCancel("H"),
    OrderStatus("I"),
    OrderUnplaced("U");
}