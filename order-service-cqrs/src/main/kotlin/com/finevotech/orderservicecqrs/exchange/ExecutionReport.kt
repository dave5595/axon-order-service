package com.finevotech.orderservicecqrs.exchange

import com.fasterxml.jackson.annotation.JsonProperty
import com.finevotech.orderservicecqrs.common.Codecs
import com.finevotech.orderservicecqrs.exchange.enum.*
import com.finevotech.orderservicecqrs.order.command.*
import java.time.ZonedDateTime

data class ExecutionReport(
    @JsonProperty("11")
    val orderId: String = "",
    @JsonProperty("41")
    val previousOrderId: String? = null,
    //id for matched order
    @JsonProperty("880")
    val tradeMatchId: String? = null,
    //order id in exchange
    @JsonProperty("37")
    val orderReferenceId: String,
    @JsonProperty("17")
    val executionId: String? = null,
    @JsonProperty("1")
    val cdsNumber: String? = null,
    @JsonProperty("44")
    val price: Double? = null,
    @JsonProperty("38")
    val quantity: Int? = null,
    @JsonProperty("14")
    val totalMatchedQuantity: Int? = null,
    @JsonProperty("151")
    val totalUnmatchedQuantity: Int? = null,
    @JsonProperty("32")
    val matchedQuantity: Int? = null,
    @JsonProperty("31")
    val matchedPrice: Double? = null,
    @JsonProperty("6")
    val averagePrice: Double? = null,
    @JsonProperty("60")
    val transactedTime: ZonedDateTime? = null,
    @JsonProperty("48")
    val securityId: String? = null,
    @JsonProperty("762")
    val securityType: String? = null,
    @JsonProperty("54")
    val orderSide: ExchangeOrderSide? = null,
    @JsonProperty("39")
    val orderStatus: ExchangeOrderStatus,
    @JsonProperty("150")
    val executionType: ExchangeExecutionType? = null,
    @JsonProperty("378")
    val executionRestatementReason: ExecutionRestatementReason? = null,
    @JsonProperty("103")
    val orderRejectionReason: String = "",
    @JsonProperty("58")
    val text: String = "", //error message on error, else return our encoded orderId
    @JsonProperty("4517")
    val contraFirm: String? = null,
    @JsonProperty("4537")
    val contraTrader: String? = null,
    @JsonProperty("4512")
    val fixId: String = "",
    @JsonProperty("4501")
    val brokerId: String = "",
    @JsonProperty("4503")
    val partyRoleClientId: String = "",
    @JsonProperty("4511")
    val partyRoleOrderOriginationTrader: String = "",


    ) {
    fun toCommand(): OrderCommand {
        when (executionType) {
            ExchangeExecutionType.New -> QueueOrderCommand(
                orderId = Codecs.decodeFromBase64(text),
                exchangeOrderId = orderId,
                previousExchangeOrderId = previousOrderId,
                exchangeReferenceId = orderReferenceId,
                executionId = executionId!!,
                cdsNumber = cdsNumber!!,
                price = price!!,
                quantity = quantity!!,
                securityId =securityId!!,
                securitySubType = securityType!!,
                fixId = fixId,
                dealerCode = partyRoleOrderOriginationTrader,
                orderSide = orderSide!!.toOrderSide(),
                brokerId = brokerId,
                executionType = executionType,
                orderStatus = orderStatus,
                text = text,
                accountId = partyRoleClientId
            )
            ExchangeExecutionType.Cancelled -> CancelOrderCommand(
                orderId = Codecs.decodeFromBase64(text),
                exchangeOrderId = orderId,
                previousExchangeOrderId = previousOrderId,
                exchangeReferenceId = orderReferenceId,
                executionId = executionId!!,
                cdsNumber = cdsNumber!!,
                price = price!!,
                quantity = quantity!!,
                securityId =securityId!!,
                securitySubType = securityType!!,
                fixId = fixId,
                dealerCode = partyRoleOrderOriginationTrader,
                orderSide = orderSide!!.toOrderSide(),
                brokerId = brokerId,
                executionType = executionType,
                orderStatus = orderStatus,
                text = text,
                accountId = partyRoleClientId
            )
            ExchangeExecutionType.Replaced -> ReviseOrderCommand(
                orderId = Codecs.decodeFromBase64(text),
                exchangeOrderId = orderId,
                previousExchangeOrderId = previousOrderId,
                exchangeReferenceId = orderReferenceId,
                executionId = executionId!!,
                cdsNumber = cdsNumber!!,
                price = price!!,
                quantity = quantity!!,
                securityId =securityId!!,
                securitySubType = securityType!!,
                fixId = fixId,
                dealerCode = partyRoleOrderOriginationTrader,
                orderSide = orderSide!!.toOrderSide(),
                brokerId = brokerId,
                executionType = executionType,
                orderStatus = orderStatus,
                text = text,
                accountId = partyRoleClientId
            )
            ExchangeExecutionType.Rejected -> RejectOrderCommand(
                orderId = Codecs.decodeFromBase64(text),
                exchangeOrderId = orderId,
                previousExchangeOrderId = previousOrderId,
                exchangeReferenceId = orderReferenceId,
                executionId = executionId!!,
                cdsNumber = cdsNumber!!,
                price = price!!,
                quantity = quantity!!,
                securityId =securityId!!,
                securitySubType = securityType!!,
                fixId = fixId,
                dealerCode = partyRoleOrderOriginationTrader,
                orderSide = orderSide?.toOrderSide(),
                brokerId = brokerId,
                executionType = executionType,
                orderStatus = orderStatus,
                text = text,
                accountId = partyRoleClientId
            )
            ExchangeExecutionType.Expired -> ExpireOrderCommand(
                orderId = Codecs.decodeFromBase64(text),
                exchangeOrderId = orderId,
                previousExchangeOrderId = previousOrderId,
                exchangeReferenceId = orderReferenceId,
                executionId = executionId!!,
                cdsNumber = cdsNumber!!,
                price = price!!,
                quantity = quantity!!,
                securityId =securityId!!,
                securitySubType = securityType!!,
                fixId = fixId,
                dealerCode = partyRoleOrderOriginationTrader,
                orderSide = orderSide!!.toOrderSide(),
                brokerId = brokerId,
                executionType = executionType,
                orderStatus = orderStatus,
                text = text,
                accountId = partyRoleClientId
            )
            ExchangeExecutionType.Trade -> FillOrderCommand(
                orderId = Codecs.decodeFromBase64(text),
                exchangeOrderId = orderId,
                previousExchangeOrderId = previousOrderId,
                exchangeReferenceId = orderReferenceId,
                executionId = executionId!!,
                cdsNumber = cdsNumber!!,
                price = price!!,
                quantity = quantity!!,
                securityId =securityId!!,
                securitySubType = securityType!!,
                contraFirm = contraFirm!!,
                contraTrader = contraTrader!!,
                fixId = fixId,
                totalMatchedQuantity = totalMatchedQuantity,
                totalUnmatchedQuantity = totalUnmatchedQuantity,
                averagePrice = averagePrice,
                accountId = partyRoleClientId,
                dealerCode = partyRoleOrderOriginationTrader,
                orderSide = orderSide!!.toOrderSide(),
                brokerId = brokerId,
                executionType = executionType,
                orderStatus = orderStatus,
                text = text,
                transactedTime = transactedTime!!
            )
            null -> TODO()
        }
        TODO()
    }
}


