package com.finevotech.orderservicecqrs.exchange

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderAction
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderSide
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderType
import com.finevotech.orderservicecqrs.exchange.enum.ExchangeOrderValidity
import java.time.ZoneOffset
import java.time.ZonedDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ExchangeOrder(
    @JsonProperty("1")
    var accountId: String = "",
    @JsonProperty("11")
    var orderId: String = "",
    @JsonProperty("41")
    var previousOrderId: String = "",
    @JsonProperty("22")
    var securityIdSource: String = "99",
    @JsonProperty("38")
    var quantity: String = "",
    @JsonProperty("40")
    var orderType: ExchangeOrderType,
    @JsonProperty("44")
    var price: String? = null,
    @JsonProperty("48")
    var securityId: String = "",
    @JsonProperty("54")
    var orderSide: ExchangeOrderSide,
    @JsonProperty("58")
    var text: String = "",
    @JsonProperty("59")
    var orderValidityType: ExchangeOrderValidity,
    @JsonProperty("528")
    var orderCapacity: String = "A",
    @JsonProperty("529")
    var orderRestrictions: String = "R",
    @JsonProperty("762")
    var securityType: String = "",
    @JsonProperty("432")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var expireDate: ZonedDateTime? = null,
    @JsonProperty("126")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var expireTime: ZonedDateTime? = null,
    //accountIdz
    @JsonProperty("4503")
    var partyRoleClientId: String = "",
    //dealer code
    @JsonProperty("4511")
    var partyRoleOrderOriginationTrader: String = "",
    @JsonProperty("-1")
    var orderAction: ExchangeOrderAction,
    @JsonProperty("-2")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var createdDate: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)
)
