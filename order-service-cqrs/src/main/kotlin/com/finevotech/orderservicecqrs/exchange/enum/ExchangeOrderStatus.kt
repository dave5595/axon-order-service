package com.finevotech.orderservicecqrs.exchange.enum

import com.fasterxml.jackson.annotation.JsonValue

enum class ExchangeOrderStatus(@get:JsonValue val status: String) {
    New("0"),
    PartialFilled("1"),
    Filled("2"),
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
    AcceptedForBidding("D"),
    PendingReplace("E")
}