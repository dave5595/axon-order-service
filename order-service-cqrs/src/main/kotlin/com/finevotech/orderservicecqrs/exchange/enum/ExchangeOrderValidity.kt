package com.finevotech.orderservicecqrs.exchange.enum

import com.fasterxml.jackson.annotation.JsonValue

enum class ExchangeOrderValidity(@get:JsonValue val status: String) {
    Day("0"),
    GoodTillCancel("1"),
    AtTheOpening("2"),
    ImmediateOrCancel("3"),
    FillOrKill("4"),
    //Not supported by bursa ATM
    //GoodTillCrossing("5"),
    GoodTillDate("6"),
    AtTheClose("7")
}