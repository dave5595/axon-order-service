package com.finevotech.orderservicecqrs.executor

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.finevotech.marketorderapigateway.common.JsonMapper
import com.finevotech.marketorderapigateway.common.Logger
import com.finevotech.orderservicecqrs.common.Codecs
import com.finevotech.orderservicecqrs.exchange.ExchangeOrder
import com.finevotech.orderservicecqrs.order.enum.convertToExchangeOrderSide
import com.yoda.mo.api.model.order.JsonOrder
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Suppress("NAME_SHADOWING")
@Profile("rabbit-executor")
@Component
@ConfigurationProperties(value = "exchange")
@EnableConfigurationProperties
class RabbitMarketOrderExecutor(
    private val executorConfigurer: ExecutorConfigurer
): InitializingBean {
    private val log by Logger()
    var channels = Channels()
    //todo store clientOrderId mapping to exchange order id

    //todo make suspending func and launch task in background
    fun execute(event: OrderExecutionRequestedEvent) {
        //todo determine order type in the future to find right fixId
        val channel = channels.usr
        if(channel.isEmpty()) {
            log.error("channel cannot be an empty string")
            return
        }
//        val executor = executorConfigurer.marketOrderExecutor(channel)
        val order = createOrder(event)
        //todo try/catch here
        JsonMapper.convertValue(order, ObjectNode::class.java).also { node ->
            val order = JsonOrder(node)
//            executor.apply { execute(order) }
//            log.info("outgoing order: $order, time: ${LocalTime.now()}")
        }
    }

    private fun createOrder(event: OrderExecutionRequestedEvent): ExchangeOrder {
        return ExchangeOrder(
            orderId = event.exchangeOrderId,
            accountId = event.cdsNumber,
            quantity = event.quantity.toString(),
            price = event.price.toString(),
            securityId = event.securityId,
            securityType = event.securitySubType,
            orderSide = event.orderSide.convertToExchangeOrderSide(),
            orderValidityType = event.orderValidity.convertToExchangeOrderValidity(),
            orderType = event.orderType.convertToExchangeOrderType(),
            orderAction = event.orderAction.toExchangeOrderAction(),
            expireDate = event.expiryDate,
            createdDate = ZonedDateTime.now(ZoneOffset.UTC),
            partyRoleClientId = event.accountId,
            partyRoleOrderOriginationTrader = event.dealerCode,
            text = Codecs.encodeToBase64(event.clientOrderId)
        )
    }

    override fun afterPropertiesSet() {
        log.info("Rabbit market order executor initialized with channels of $channels")
    }
}

class Channels {
    lateinit var usr: String
    lateinit var dbt: String

    override fun toString(): String {
        return "Channels(usr='$usr', dbt='$dbt')"
    }


}

