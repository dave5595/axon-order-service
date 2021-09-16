package com.finevotech.orderservicecqrs.order.enum

import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderValidity

enum class OrderValidity{
    SessionOrDay,
    GoodTillCancel,
    AtTheOpening,
    ImmediateOrCancel,
    FillOrKill,
    GoodTillDate,
    AtTheClose;

    fun convertToExchangeOrderValidity(): ExchangeOrderValidity {
        return when(this){
            GoodTillDate -> ExchangeOrderValidity.GoodTillDate
            SessionOrDay -> ExchangeOrderValidity.Day
            GoodTillCancel -> ExchangeOrderValidity.GoodTillCancel
            AtTheOpening -> ExchangeOrderValidity.AtTheOpening
            ImmediateOrCancel -> ExchangeOrderValidity.ImmediateOrCancel
            FillOrKill -> ExchangeOrderValidity.FillOrKill
            AtTheClose -> ExchangeOrderValidity.AtTheClose
        }
    }
}

